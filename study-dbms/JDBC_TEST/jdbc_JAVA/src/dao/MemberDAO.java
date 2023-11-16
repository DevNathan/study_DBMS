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

//   1. ����
	Connection connection;
//   2. ���� ����
	PreparedStatement preparedStatement;
//   3. ���
	ResultSet resultSet;

//   ���̵� �ߺ��˻�
//   ȭ�鿡�� ����ڰ� �Է��� ���̵� ���޹޴´�.
	public boolean checkId(String memberId) {
		String query = "SELECT ID FROM TBL_MEMBER WHERE MEMBER_ID = ?";
//      �ߺ� �� false, �ߺ� ���� �� true
		boolean check = false;
		try {
//         ���� ��ü �ޱ�
			connection = DBConnecter.getConnection();
//         �ۼ��� ���� �����ϱ�
			preparedStatement = connection.prepareStatement(query);
//         ������ ���� �ϼ��ϱ�
			preparedStatement.setString(1, memberId);
//         ��� ���
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// ��� ���� ���� ���� ���� ������
				resultSet.getLong(1);
			} else {
				// ��� ���� ������ �ߺ� �������� ó��
				check = true;
			}
		} catch (SQLException e) {
			System.out.println("checkId(String) SQL�� ����");
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

//   ȸ������
//   executeUpdate()�� �����ϱ�!
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

//         INSERT, DELETE, UPDATE �� ������ ���� executeUpdate()�� ����Ѵ�.
//         SELECT�� executeQuery()�� ����Ѵ�.
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				System.out.println("ȸ�������� �Ϸ� �Ǿ����ϴ�.");
			} else
				System.out.println("ȸ�������� �����Ͽ����ϴ�.");

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

//   �α���
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
				check = false; // �α��� ����
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

//   �α׾ƿ�
	public void logout() {
		session = null;
	}

//   ����������
	public MemberVO findById() {
		String query = "SELECT ID, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_ADDRESS, MEMBER_RECOMMENDER_ID "
				+ "FROM TBL_MEMBER WHERE ID = ?";
		MemberVO memberVO = new MemberVO();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
//         �α��� �� ���������� ���񽺸� �̿��� �� �ֱ� ������
//         session�� �ִ� ȸ�� ������ ������������ �ʿ��� ������ ��ȸ�Ѵ�.
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

//   ���� ����
//  ��й�ȣ ����
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
				System.out.println("ȸ�� ������ ������Ʈ�Ǿ����ϴ�.");
			} else {
				System.out.println("ȸ�� ���� ������Ʈ�� �����߽��ϴ�.");
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

//   ȸ��Ż��
	public void deleteAccount() {
		String query = "DELETE FROM TBL_MEMBER WHERE ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, session);

			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				System.out.println("ȸ�� ������ �����Ǿ����ϴ�.");
			} else {
				System.out.println("ȸ�� ���� ������ �����߽��ϴ�.");
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

//   ��õ��
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
	
//   ���� ��õ�� ���
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
