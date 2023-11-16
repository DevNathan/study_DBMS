package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vo.MemberVO;
import vo.PostDTO;
import vo.PostVO;

public class PostDAO {
	public static Long postSession;
	
//  1. 연결
	Connection connection;
//  2. 쿼리 실행
	PreparedStatement preparedStatement;
//  3. 결과
	ResultSet resultSet;

//	게시글 등록
	public void post(PostVO postVO) {
		String query = "INSERT INTO TBL_POST " + "(ID, POST_TITLE, POST_CONTENT, MEMBER_ID) "
				+ "VALUES(SEQ_POST.NEXTVAL, ?, ?, ?)";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, postVO.getPostTitle());
			preparedStatement.setString(2, postVO.getPostContent());
			preparedStatement.setLong(3, MemberDAO.session);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
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

//	게시글 전체목록
	public List<PostDTO> viewAllPostsName() {
		String query = "SELECT P.ID, POST_TITLE, M.MEMBER_NAME FROM TBL_POST P "
			    + "JOIN TBL_MEMBER M ON P.MEMBER_ID = M.ID ORDER BY P.ID";
		List<PostDTO> posts = new ArrayList<>();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				PostDTO postDTO = new PostDTO();
				postDTO.setId(resultSet.getLong("ID"));
				postDTO.setPostTitle(resultSet.getString("POST_TITLE"));
				postDTO.setMemberName(resultSet.getString("MEMBER_NAME"));
				posts.add(postDTO);
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
		return posts;
	}

//	게시글 조회(회원의 이름까지 조회)
	public PostDTO findById(Long id) {
		String query = "SELECT P.ID, POST_TITLE, POST_CONTENT, M.MEMBER_ID, M.MEMBER_NAME "
				+ "FROM TBL_MEMBER M JOIN TBL_POST P " + "ON M.ID = P.MEMBER_ID AND P.ID = ?";
		PostDTO postDTO = new PostDTO();
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			resultSet = preparedStatement.executeQuery();

			resultSet.next();

			postDTO.setId(resultSet.getLong(1));
			postDTO.setPostTitle(resultSet.getString(2));
			postDTO.setPostContent(resultSet.getString(3));
			postDTO.setMemberIdentification(resultSet.getString(4));
			postDTO.setMemberName(resultSet.getString(5));

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
				throw new RuntimeException(e);
			}
		}

		return postDTO;

	}

//	게시글 수정(제목과 내용만 수정 가능)
	public void postUpdate(PostVO postVO) {
		String query = "UPDATE TBL_POST SET POST_TITLE = ?, POST_CONTENT = ? " + "WHERE ID = ?";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, postVO.getPostTitle());
			preparedStatement.setString(2, postVO.getPostContent());
			preparedStatement.setLong(3, PostDAO.postSession);
			preparedStatement.executeUpdate();

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

//	게시글 삭제
	public void postDelete(Long id) {
		String query = "DELETE FROM TBL_POST WHERE ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
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
	
//	게시글 작성자가 수정을 하고자 하는 작성자인지 id확인
	public boolean checkMemberId() {
		boolean flag = false;
		String query = "SELECT M.ID FROM TBL_POST P "
				+ "JOIN TBL_MEMBER M "
				+ "ON P.MEMBER_ID = M.ID "
				+ "WHERE P.ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, PostDAO.postSession);
			
			resultSet = preparedStatement.executeQuery();
			
	        if (resultSet.next()) {
	            long memberIdFromPost = resultSet.getLong(1);
	            if (MemberDAO.session == memberIdFromPost) {
	                flag = true;
	            }
	        }
			
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
		
		return flag;
	}
	
//	개시글과 개시글 댓글 수 출력
	public List<PostDTO> getListWithReplyCount() {
	    String query = "SELECT P.ID, P.POST_TITLE, P.POST_CONTENT, M.MEMBER_ID, M.MEMBER_NAME, COUNT(R.ID) AS REPLY_COUNT " +
	            "FROM TBL_POST P " +
	            "LEFT OUTER JOIN TBL_REPLY R ON P.ID = R.POST_ID " +
	            "JOIN TBL_MEMBER M ON P.MEMBER_ID = M.ID " +
	            "GROUP BY P.ID, P.POST_TITLE, P.POST_CONTENT, M.MEMBER_ID, M.MEMBER_NAME";

	    ArrayList<PostDTO> posts = new ArrayList<>();

	    try {
	        connection = DBConnecter.getConnection();
	        preparedStatement = connection.prepareStatement(query);
	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            PostDTO postDTO = new PostDTO(); // Create a new instance for each post
	            postDTO.setId(resultSet.getLong("ID"));
	            postDTO.setPostTitle(resultSet.getString("POST_TITLE"));
	            postDTO.setPostContent(resultSet.getString("POST_CONTENT"));
	            postDTO.setMemberIdentification(resultSet.getString("MEMBER_ID"));
	            postDTO.setMemberName(resultSet.getString("MEMBER_NAME"));
	            postDTO.setReplyCount(resultSet.getInt("REPLY_COUNT"));

	            posts.add(postDTO);
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
	            throw new RuntimeException(e);
	        }
	    }

	    return posts;
	}
}
