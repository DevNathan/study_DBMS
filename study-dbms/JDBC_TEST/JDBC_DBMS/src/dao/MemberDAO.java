package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vo.MemberVO;

public class MemberDAO {
	public static Long session;

//   1. 연결
	Connection connection;
//   2. 쿼리 실행
	PreparedStatement preparedStatement;
//   3. 결과
	ResultSet resultSet;

//   아이디 중복검사
//   화면에서 사용자가 입력한 아이디를 전달받는다.
	public boolean checkId(String memberId) {
		String query = "SELECT ID FROM TBL_MEMBER WHERE MEMBER_ID = ?";
//      중복 시 false, 중복 없을 시 true
		boolean check = false;
		try {
//         연결 객체 받기
			connection = DBConnecter.getConnection();
//         작성한 쿼리 전달하기
			preparedStatement = connection.prepareStatement(query);
//         전달한 쿼리 완성하기
			preparedStatement.setString(1, memberId);
//         결과 담기
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// 결과 행이 있을 때만 열을 가져옴
				resultSet.getLong(1);
			} else {
				// 결과 행이 없으면 중복 없음으로 처리
				check = true;
			}
		} catch (SQLException e) {
			System.out.println("checkId(String) SQL문 오류");
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		return check;
	}

//   회원가입
//   executeUpdate()로 실행하기!
	public void join(MemberVO memberVO) {
		String query = "INSERT INTO TBL_MEMBER " + "(ID, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_ADDRESS) "
				+ "VALUES(SEQ_MEMBER.NEXTVAL, ?, ?, ?, ?)";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, memberVO.getMemberId());
			preparedStatement.setString(2, memberVO.getMemberPassword());
			preparedStatement.setString(3, memberVO.getMemberName());
			preparedStatement.setString(4, memberVO.getMemberAddress());

//         INSERT, DELETE, UPDATE 세 가지는 전부 executeUpdate()를 사용한다.
//         SELECT만 executeQuery()를 사용한다.
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				System.out.println("회원가입이 완료 되었습니다.");
			} else
				System.out.println("회원가입을 실패하였습니다.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

//   로그인
	public boolean login(MemberVO memberVO) {
		String query = "SELECT ID FROM TBL_MEMBER WHERE MEMBER_ID = ? AND MEMBER_PASSWORD = ?";
		connection = DBConnecter.getConnection();
		boolean check = true;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, memberVO.getMemberId());
			preparedStatement.setString(2, memberVO.getMemberPassword());

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				session = resultSet.getLong(1);
			} else {
				check = false; // 로그인 실패
			}
		} catch (SQLException e) {
			e.printStackTrace();
			check = false;
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		return check;
	}

//   로그아웃
	public void logout() {
		session = null;
	}

//   마이페이지
	public MemberVO findById() {
		String query = "SELECT ID, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_ADDRESS, MEMBER_RECOMMENDER_ID "
				+ "FROM TBL_MEMBER WHERE ID = ?";
		MemberVO memberVO = new MemberVO();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
//         로그인 후 마이페이지 서비스를 이용할 수 있기 때문에
//         session에 있는 회원 정보로 마이페이지에 필요한 정보를 조회한다.
			preparedStatement.setLong(1, session);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				memberVO.setId(resultSet.getLong("ID"));
				memberVO.setMemberId(resultSet.getString("MEMBER_ID"));
				memberVO.setMemberPassword(resultSet.getString("MEMBER_PASSWORD"));
				memberVO.setMemberName(resultSet.getString("MEMBER_NAME"));
				memberVO.setMemberAddress(resultSet.getString("MEMBER_ADDRESS"));
				memberVO.setRecommenderId(resultSet.getString("MEMBER_RECOMMENDER_ID"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}

		return memberVO;
	}

//   정보 수정
//  비밀번호 변경
	public void update(int select, String changedInformation) {
		String query = "";
		MemberVO memberVO = new MemberVO();

		try {
			connection = DBConnecter.getConnection();
			if (select == 1) {
				query = "UPDATE TBL_MEMBER SET MEMBER_NAME = ? WHERE ID = ?";
			} else if (select == 2) {
				query = "UPDATE TBL_MEMBER SET MEMBER_PASSWORD = ? WHERE ID = ?";
			} else if (select == 3) {
				query = "UPDATE TBL_MEMBER SET MEMBER_ADDRESS = ? WHERE ID = ?";
			}
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, changedInformation);
			preparedStatement.setLong(2, session);

			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				System.out.println("회원 정보가 업데이트되었습니다.");
			} else {
				System.out.println("회원 정보 업데이트에 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

//   회원탈퇴
	public void deleteAccount() {
		String query = "DELETE FROM TBL_MEMBER WHERE ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, session);

			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				System.out.println("회원 정보가 삭제되었습니다.");
			} else {
				System.out.println("회원 정보 삭제에 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

//   추천수
	public Object[] recommendCount() {
	    String query = "SELECT COUNT(MEMBER_RECOMMENDER_ID) FROM TBL_MEMBER "
	            + "WHERE MEMBER_RECOMMENDER_ID = (SELECT MEMBER_ID FROM TBL_MEMBER WHERE ID = ?) "
	            + "GROUP BY MEMBER_RECOMMENDER_ID";
	    String query2 = "SELECT MEMBER_NAME "
	            + "FROM TBL_MEMBER "
	            + "WHERE MEMBER_RECOMMENDER_ID IN (SELECT MEMBER_ID FROM TBL_MEMBER WHERE ID = ?) "
	            + "GROUP BY MEMBER_NAME ORDER BY MEMBER_NAME";
	    int count = 0;
	    List<String> names = new ArrayList<>();

	    try {
	        connection = DBConnecter.getConnection();
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setLong(1, session);

	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            count = resultSet.getInt(1);
	        }

	        preparedStatement = connection.prepareStatement(query2);
	        preparedStatement.setLong(1, session);
	        resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	            String name = resultSet.getString(1);
	            names.add(name);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException();
	        }
	    }

	    Object[] result = new Object[2];
	    result[0] = count;
	    result[1] = names.toArray(new String[0]);
	    return result;
	}
	
//   나를 추천한 사람
	public String viewMyRecommender() {
	    String query = "SELECT MEMBER_NAME "
	            + "FROM TBL_MEMBER "
	            + "WHERE MEMBER_ID = "
	            + "(SELECT MEMBER_RECOMMENDER_ID FROM TBL_MEMBER WHERE ID = ?)";
	    String returnStr = "";

	    try {
	        connection = DBConnecter.getConnection();
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setLong(1, session);

	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            returnStr = resultSet.getString(1);
	        } else {
	            returnStr = null;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException();
	        }
	    }
	    return returnStr;
	}

}
