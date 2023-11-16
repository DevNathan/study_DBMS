package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import vo.PhonebookVO;

public class PhonebookDAO {

//  1. 연결
	Connection connection;
//  2. 쿼리 실행
	PreparedStatement preparedStatement;
//  3. 결과
	ResultSet resultSet;

	public List<PhonebookVO> printPhonebook() {
		String query = "SELECT NAME, PHONE_NUMBER, AGE, TO_CHAR(BIRTHDATE, 'YYYY-MM-DD') AS BIRTHDATE "
				+ "FROM TBL_PHONEBOOK ORDER BY NAME, PHONE_NUMBER";
		List<PhonebookVO> list = new ArrayList<PhonebookVO>();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				PhonebookVO phonebookVO = new PhonebookVO();
				phonebookVO.setName(resultSet.getString("NAME"));
				phonebookVO.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
				phonebookVO.setAge(resultSet.getInt("AGE"));
				phonebookVO.setBirthDate(resultSet.getNString("BIRTHDATE"));
				list.add(phonebookVO);
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
		return list;
	}

	public boolean addPhoneNumber(PhonebookVO phonebookVO) {
		String query = "INSERT INTO TBL_PHONEBOOK (NAME, PHONE_NUMBER, AGE, BIRTHDATE) " + "VALUES (?, ?, ?, ?)";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, phonebookVO.getName());
			preparedStatement.setString(2, phonebookVO.getPhoneNumber());
			preparedStatement.setInt(3, phonebookVO.getAge());
			preparedStatement.setString(4, phonebookVO.getBirthDate());

			int rowsaffected = preparedStatement.executeUpdate();
			return rowsaffected > 0;
			
		} catch (SQLException e) {
				e.printStackTrace();
				return false;
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

	public List<PhonebookVO> getPhoneNumberOfSameName(String name) {
		String query = "SELECT NAME, PHONE_NUMBER, AGE, TO_CHAR(BIRTHDATE, 'YYYY-MM-DD') AS BIRTHDATE "
				+ "FROM TBL_PHONEBOOK WHERE NAME = ? ORDER BY PHONE_NUMBER";
		List<PhonebookVO> list = new ArrayList<>();

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				PhonebookVO phonebookVO = new PhonebookVO();
				phonebookVO.setName(resultSet.getString("NAME"));
				phonebookVO.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
				phonebookVO.setAge(resultSet.getInt("AGE"));
				phonebookVO.setBirthDate(resultSet.getNString("BIRTHDATE"));
				list.add(phonebookVO);
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

		return list;
	}

	public boolean updatePhoneNumber(PhonebookVO phonebookVO, String originalName, String originalPhoneNumber,
	        int select) {
	    String query = "";

	    try {
	        connection = DBConnecter.getConnection();
	        if (select == 1) {
	            query = "UPDATE TBL_PHONEBOOK SET NAME = ? WHERE NAME = ? AND PHONE_NUMBER = ?";
	            preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, phonebookVO.getName());
	            preparedStatement.setString(2, originalName);
	            preparedStatement.setString(3, originalPhoneNumber);
	        } else if (select == 2) {
	            query = "UPDATE TBL_PHONEBOOK SET PHONE_NUMBER = ? WHERE NAME = ? AND PHONE_NUMBER = ?";
	            preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, phonebookVO.getPhoneNumber());
	            preparedStatement.setString(2, originalName);
	            preparedStatement.setString(3, originalPhoneNumber);
	        } else if (select == 3) {
	            query = "UPDATE TBL_PHONEBOOK SET AGE = ? WHERE NAME = ? AND PHONE_NUMBER = ?";
	            preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setInt(1, phonebookVO.getAge());
	            preparedStatement.setString(2, originalName);
	            preparedStatement.setString(3, originalPhoneNumber);
	        } else if (select == 4) {
	            query = "UPDATE TBL_PHONEBOOK SET BIRTHDATE = ? WHERE NAME = ? AND PHONE_NUMBER = ?";
	            preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, phonebookVO.getBirthDate());
	            preparedStatement.setString(2, originalName);
	            preparedStatement.setString(3, originalPhoneNumber);
	        }

	        int rowsAffected = preparedStatement.executeUpdate();

	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        if (e instanceof SQLIntegrityConstraintViolationException) {
	            System.out.println("이미 등록된 전화번호입니다.");
	        } else {
	            e.printStackTrace();
	        }
	        return false; // SQLException이 발생하면 false를 반환하고, 이후 로직은 처리하지 않음
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
	
	public boolean deletePhoneNumber(String name, String phoneNumber) {
		String query = "DELETE FROM TBL_PHONEBOOK WHERE NAME = ? AND PHONE_NUMBER = ?";

		try {
			connection = DBConnecter.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, phoneNumber);

	        int rowsaffected = preparedStatement.executeUpdate();
	        return rowsaffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
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
}
