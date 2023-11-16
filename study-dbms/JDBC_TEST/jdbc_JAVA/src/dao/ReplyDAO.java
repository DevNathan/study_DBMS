package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vo.ReplyDTO;
import vo.ReplyVO;

public class ReplyDAO {
//  1. 연결
	Connection connection;
//  2. 쿼리 실행
	PreparedStatement preparedStatement;
//  3. 결과
	ResultSet resultSet;

	public void reply(ReplyVO replyVO) {
		String query = "INSERT INTO TBL_REPLY" + "(ID, REPLY_CONTENT, POST_ID, MEMBER_ID) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, ?, ?)";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, replyVO.getReplyContent());
			preparedStatement.setLong(2, PostDAO.postSession);
			preparedStatement.setLong(3, MemberDAO.session);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
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

	public List<ReplyDTO> viewAllReplies() {
		String query = "SELECT R.ID, M.MEMBER_NAME, R.REPLY_CONTENT FROM TBL_REPLY R JOIN TBL_MEMBER M "
				+ "ON M.ID = R.MEMBER_ID WHERE POST_ID = ? ORDER BY ID";

		List<ReplyDTO> replies = new ArrayList<>();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, PostDAO.postSession);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				ReplyDTO replyDTO = new ReplyDTO();
				replyDTO.setId(resultSet.getLong("ID"));
				replyDTO.setMemberName(resultSet.getString("MEMBER_NAME"));
				replyDTO.setReplyContent(resultSet.getString("REPLY_CONTENT"));
				replies.add(replyDTO);
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
		return replies;
	}

	public void updateReply(String changedReply, Long ReplyId) {
		String query = "UPDATE TBL_REPLY SET REPLY_CONTENT = ? WHERE ID = ? AND MEMBER_ID = ?";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, changedReply);
			preparedStatement.setLong(2, ReplyId);
			preparedStatement.setLong(3, MemberDAO.session);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
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
	
	public void deleteReply(Long replyId) {
		String query = "DELETE FROM TBL_REPLY WHERE ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, replyId);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
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
	
	public Object[] viewAllRepliesAndCount(Long post_id) {
	    String query = "SELECT M.MEMBER_NAME, R.REPLY_CONTENT " + "FROM TBL_REPLY R " + "JOIN TBL_MEMBER M "
	            + "ON M.ID = R.MEMBER_ID " + "WHERE POST_ID = ?";
	    String query2 = "SELECT COUNT(ID) FROM TBL_REPLY WHERE POST_ID = ?";

	    List<ReplyDTO> replies = new ArrayList<>();
	    int count = 0;

	    try {
	        connection = DBConnecter.getConnection();
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setLong(1, post_id);
	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            ReplyDTO replyDTO = new ReplyDTO();
	            replyDTO.setMemberName(resultSet.getString("MEMBER_NAME"));
	            replyDTO.setReplyContent(resultSet.getString("REPLY_CONTENT"));
	            replies.add(replyDTO);
	        }

	        preparedStatement = connection.prepareStatement(query2);
	        preparedStatement.setLong(1, post_id);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            count = resultSet.getInt(1);
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
	    result[0] = replies;
	    result[1] = count;
	    return result;
	}
	
	public boolean checkMemberId(Long replyId) {
		boolean flag = false;
		String query = "SELECT M.ID FROM TBL_REPLY R "
				+ "JOIN TBL_MEMBER M "
				+ "ON R.MEMBER_ID = M.ID "
				+ "WHERE R.ID = ?";
		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, replyId);
			
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
}