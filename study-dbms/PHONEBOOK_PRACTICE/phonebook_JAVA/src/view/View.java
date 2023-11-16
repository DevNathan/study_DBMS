package view;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dao.PhonebookDAO;
import vo.PhonebookVO;

public class View {
	static Scanner sc = new Scanner(System.in);

	private void menu1() {
		System.out.println("=== 전화번호 부 with DB ===");
		System.out.println("1. 전화번호부 출력 | 2. 전화번호 추가 | 3. 기존 정보 변경 | 4. 전화번호 제거 | q. 프로그램 종료");
	}

	public static void main(String[] args) {
		View view = new View();
		char menuController = 0;

		while (menuController != '5') {
			view.menu1();
			menuController = sc.next().charAt(0);
			sc.nextLine();

			switch (menuController) {
			case '1':
				view.printList();
				break;
			case '2':
				view.addPhoneNumber();
				break;
			case '3':
				String[] origin = view.findSameName();
				if (origin != null) {
					view.update(origin[0], origin[1]);
				}
				break;
			case '4':
			    String[] nameAndPhoneNumber = view.findSameName();
			    if (nameAndPhoneNumber != null) {
			    	boolean deleteResult = false;
			        String nameToDelete = nameAndPhoneNumber[0];
			        String phoneNumberToDelete = nameAndPhoneNumber[1];
			        PhonebookDAO phonebookDAO = new PhonebookDAO();
			        deleteResult = phonebookDAO.deletePhoneNumber(nameToDelete, phoneNumberToDelete);
			        if (deleteResult) {
			            System.out.println("삭제 성공");
			        } else {
			            System.out.println("삭제 실패, 다시 시도해주세요.");
			        }
			    }
			    break;
			case 'q':
				System.out.println("프로그램 종료");
				return;
			default:
				System.out.println("잘못된 입력 값");
			}
			System.out.println();
		}
	}

	private void printList() {
		PhonebookDAO phonebookDAO = new PhonebookDAO();
		List<PhonebookVO> list = phonebookDAO.printPhonebook();

		list.forEach(row -> System.out.println(
				row.getName() + ", " + row.getPhoneNumber() + ", " + row.getAge() + ", " + row.getBirthDate()));
	}

	public void addPhoneNumber() {
		PhonebookDAO phonebookDAO = new PhonebookDAO();
		PhonebookVO phonebookVO = new PhonebookVO();

		System.out.print("(필수)추가할 전화번호 소유자의 이름을 입력하세요. : ");
		String name = sc.nextLine();
		// 이름이 빈 문자열인 경우 다시 입력 받도록 처리
		while (name.trim().isEmpty()) {
			System.out.println("이름은 필수 입력 사항입니다. 다시 입력하세요.");
			System.out.print("(필수)추가할 전화번호 소유자의 이름을 입력하세요. : ");
			name = sc.nextLine();
		}
		phonebookVO.setName(name);

		System.out.print("(필수)추가할 전화번호를 입력하세요(공백과 - 없이 예)01012345678). : ");
		String phoneNumber = sc.nextLine();
		// 전화번호 빈 문자열인 경우 다시 입력 받도록 처리
		while (phoneNumber.trim().isEmpty()) {
			System.out.println("전화번호는 필수 입력 사항입니다. 다시 입력하세요.");
			System.out.print("(필수)추가할 전화번호를 입력하세요(공백과 - 없이 예)01012345678). : ");
			phoneNumber = sc.nextLine();
		}
		phonebookVO.setPhoneNumber(phoneNumber);

		System.out.print("나이를 입력하세요(추가하지 않을 시 Enter). : ");
		String ageInput = sc.nextLine();
		if (!ageInput.isEmpty()) {
			try {
				phonebookVO.setAge(Integer.parseInt(ageInput));
			} catch (NumberFormatException e) {
				System.out.println("정상적인 입력값이 아닙니다.");
				return;
			}
		}

		System.out.print("생일을 입력하세요(yyyy-mm-dd)(추가하지 않을 시 Enter). : ");
		String birthDateInput = sc.nextLine();
		if (!birthDateInput.isEmpty()) {
			phonebookVO.setBirthDate(birthDateInput);
		}

		if (phonebookDAO.addPhoneNumber(phonebookVO)) {
			System.out.println("전화번호 추가 완료");
		} else {
			System.out.println("전화번호 추가 실패");
		}
	}

//	동명이인 찾는 메소드
	private String[] findSameName() {
		PhonebookDAO phonebookDAO = new PhonebookDAO();

		System.out.print("기존에 저장된 전화번호 정보의 이름을 입력해주세요. : ");
		String name = sc.nextLine();
		List<PhonebookVO> list = phonebookDAO.getPhoneNumberOfSameName(name);

		if (list.size() == 1) {
			PhonebookVO phonebookVO = list.get(0);
			String[] origin = { name, phonebookVO.getPhoneNumber() };
			return origin;
		} else if (list.size() > 1) {

			System.out.println("동명이인이 발견되었습니다.");

			Map<Integer, String> map = new HashMap<>();

			for (int i = 0; i < list.size(); i++) {
				PhonebookVO phonebookVO = list.get(i); // 리스트에서 i번째 PhonebookVO 객체 가져오기
				System.out.println((i + 1) + ". " + phonebookVO.getName() + ", " + phonebookVO.getPhoneNumber());
				map.put(i + 1, phonebookVO.getPhoneNumber());
			}

			System.out.print("수정을 원하는 정보의 맨 앞에 붙은 번호를 입력해주세요. : ");
			int selectedMenu = Integer.parseInt(sc.next());

			if (selectedMenu < 1 || selectedMenu > list.size()) {
				System.out.println("잘못된 선택입니다. 1부터 " + list.size() + " 사이의 번호를 선택해주세요.");
				return null;
			}
			String[] origin = { name, map.get(selectedMenu) };
			return origin;
		} else {
			System.out.println("정보가 없습니다.");
			return null;
		}
	}

	private void update(String originalName, String originalPhoneNumber) {
		PhonebookDAO phonebookDAO = new PhonebookDAO();
		PhonebookVO phonebookVO = new PhonebookVO();
		char menuController = 0;

		while (menuController != 'e') {
			boolean updateResult = false;
			System.out.println("1. 이름 변경 | 2. 전화번호 변경 | 3. 나이 변경 | 4. 생일 변경 | q. 뒤로가기");
			menuController = sc.next().charAt(0);
			sc.nextLine();

			switch (menuController) {
			case '1':
				System.out.print("변경할 이름을 입력해주세요: ");
				String newName = sc.nextLine();
				while (newName.trim().isEmpty()) {
					System.out.println("이름은 필수 입력 사항입니다. 다시 입력하세요.");
					System.out.print("변경할 이름을 입력해주세요: ");
					newName = sc.nextLine();
				}
				phonebookVO.setName(newName);
				updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 1);
				originalName = newName;
				break;
			case '2':
				System.out.print("변경할 전화번호를 입력해주세요: ");
				String newPhoneNumber = sc.nextLine();
				while (newPhoneNumber.trim().isEmpty()) {
					System.out.println("전화번호는 필수 입력 사항입니다. 다시 입력하세요.");
					System.out.print("변경할 전화번호를 입력해주세요: ");
					newPhoneNumber = sc.nextLine();
				}
				phonebookVO.setPhoneNumber(newPhoneNumber);
				updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 2);
				originalPhoneNumber = newPhoneNumber;
				break;
			case '3':
				System.out.print("변경할 나이 정보를 입력하세요(추가하지 않을 시 Enter): ");
				String ageInput = sc.nextLine();
				if (!ageInput.isEmpty()) {
					try {
						phonebookVO.setAge(Integer.parseInt(ageInput));
						updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber,
								3);
					} catch (NumberFormatException e) {
						System.out.println("정상적인 입력값이 아닙니다.");
					}
				}
				break;
			case '4':
				System.out.print("변경할 생일 정보를 입력하세요(yyyy-mm-dd)(추가하지 않을 시 Enter): ");
				String birthDateInput = sc.nextLine();
				if (!birthDateInput.isEmpty()) {
					phonebookVO.setBirthDate(birthDateInput);
					updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 4);
				}
				break;
			case 'q':
				return;
			default:
				System.out.println("잘못된 입력값입니다.");
			}
			if (updateResult) {
				System.out.println("정보 변경 완료");
			} else {
				System.out.println("정보 변경 실패");
			}
		}
	}
}
