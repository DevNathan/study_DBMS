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
		System.out.println("=== ��ȭ��ȣ �� with DB ===");
		System.out.println("1. ��ȭ��ȣ�� ��� | 2. ��ȭ��ȣ �߰� | 3. ���� ���� ���� | 4. ��ȭ��ȣ ���� | q. ���α׷� ����");
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
			            System.out.println("���� ����");
			        } else {
			            System.out.println("���� ����, �ٽ� �õ����ּ���.");
			        }
			    }
			    break;
			case 'q':
				System.out.println("���α׷� ����");
				return;
			default:
				System.out.println("�߸��� �Է� ��");
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

		System.out.print("(�ʼ�)�߰��� ��ȭ��ȣ �������� �̸��� �Է��ϼ���. : ");
		String name = sc.nextLine();
		// �̸��� �� ���ڿ��� ��� �ٽ� �Է� �޵��� ó��
		while (name.trim().isEmpty()) {
			System.out.println("�̸��� �ʼ� �Է� �����Դϴ�. �ٽ� �Է��ϼ���.");
			System.out.print("(�ʼ�)�߰��� ��ȭ��ȣ �������� �̸��� �Է��ϼ���. : ");
			name = sc.nextLine();
		}
		phonebookVO.setName(name);

		System.out.print("(�ʼ�)�߰��� ��ȭ��ȣ�� �Է��ϼ���(����� - ���� ��)01012345678). : ");
		String phoneNumber = sc.nextLine();
		// ��ȭ��ȣ �� ���ڿ��� ��� �ٽ� �Է� �޵��� ó��
		while (phoneNumber.trim().isEmpty()) {
			System.out.println("��ȭ��ȣ�� �ʼ� �Է� �����Դϴ�. �ٽ� �Է��ϼ���.");
			System.out.print("(�ʼ�)�߰��� ��ȭ��ȣ�� �Է��ϼ���(����� - ���� ��)01012345678). : ");
			phoneNumber = sc.nextLine();
		}
		phonebookVO.setPhoneNumber(phoneNumber);

		System.out.print("���̸� �Է��ϼ���(�߰����� ���� �� Enter). : ");
		String ageInput = sc.nextLine();
		if (!ageInput.isEmpty()) {
			try {
				phonebookVO.setAge(Integer.parseInt(ageInput));
			} catch (NumberFormatException e) {
				System.out.println("�������� �Է°��� �ƴմϴ�.");
				return;
			}
		}

		System.out.print("������ �Է��ϼ���(yyyy-mm-dd)(�߰����� ���� �� Enter). : ");
		String birthDateInput = sc.nextLine();
		if (!birthDateInput.isEmpty()) {
			phonebookVO.setBirthDate(birthDateInput);
		}

		if (phonebookDAO.addPhoneNumber(phonebookVO)) {
			System.out.println("��ȭ��ȣ �߰� �Ϸ�");
		} else {
			System.out.println("��ȭ��ȣ �߰� ����");
		}
	}

//	�������� ã�� �޼ҵ�
	private String[] findSameName() {
		PhonebookDAO phonebookDAO = new PhonebookDAO();

		System.out.print("������ ����� ��ȭ��ȣ ������ �̸��� �Է����ּ���. : ");
		String name = sc.nextLine();
		List<PhonebookVO> list = phonebookDAO.getPhoneNumberOfSameName(name);

		if (list.size() == 1) {
			PhonebookVO phonebookVO = list.get(0);
			String[] origin = { name, phonebookVO.getPhoneNumber() };
			return origin;
		} else if (list.size() > 1) {

			System.out.println("���������� �߰ߵǾ����ϴ�.");

			Map<Integer, String> map = new HashMap<>();

			for (int i = 0; i < list.size(); i++) {
				PhonebookVO phonebookVO = list.get(i); // ����Ʈ���� i��° PhonebookVO ��ü ��������
				System.out.println((i + 1) + ". " + phonebookVO.getName() + ", " + phonebookVO.getPhoneNumber());
				map.put(i + 1, phonebookVO.getPhoneNumber());
			}

			System.out.print("������ ���ϴ� ������ �� �տ� ���� ��ȣ�� �Է����ּ���. : ");
			int selectedMenu = Integer.parseInt(sc.next());

			if (selectedMenu < 1 || selectedMenu > list.size()) {
				System.out.println("�߸��� �����Դϴ�. 1���� " + list.size() + " ������ ��ȣ�� �������ּ���.");
				return null;
			}
			String[] origin = { name, map.get(selectedMenu) };
			return origin;
		} else {
			System.out.println("������ �����ϴ�.");
			return null;
		}
	}

	private void update(String originalName, String originalPhoneNumber) {
		PhonebookDAO phonebookDAO = new PhonebookDAO();
		PhonebookVO phonebookVO = new PhonebookVO();
		char menuController = 0;

		while (menuController != 'e') {
			boolean updateResult = false;
			System.out.println("1. �̸� ���� | 2. ��ȭ��ȣ ���� | 3. ���� ���� | 4. ���� ���� | q. �ڷΰ���");
			menuController = sc.next().charAt(0);
			sc.nextLine();

			switch (menuController) {
			case '1':
				System.out.print("������ �̸��� �Է����ּ���: ");
				String newName = sc.nextLine();
				while (newName.trim().isEmpty()) {
					System.out.println("�̸��� �ʼ� �Է� �����Դϴ�. �ٽ� �Է��ϼ���.");
					System.out.print("������ �̸��� �Է����ּ���: ");
					newName = sc.nextLine();
				}
				phonebookVO.setName(newName);
				updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 1);
				originalName = newName;
				break;
			case '2':
				System.out.print("������ ��ȭ��ȣ�� �Է����ּ���: ");
				String newPhoneNumber = sc.nextLine();
				while (newPhoneNumber.trim().isEmpty()) {
					System.out.println("��ȭ��ȣ�� �ʼ� �Է� �����Դϴ�. �ٽ� �Է��ϼ���.");
					System.out.print("������ ��ȭ��ȣ�� �Է����ּ���: ");
					newPhoneNumber = sc.nextLine();
				}
				phonebookVO.setPhoneNumber(newPhoneNumber);
				updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 2);
				originalPhoneNumber = newPhoneNumber;
				break;
			case '3':
				System.out.print("������ ���� ������ �Է��ϼ���(�߰����� ���� �� Enter): ");
				String ageInput = sc.nextLine();
				if (!ageInput.isEmpty()) {
					try {
						phonebookVO.setAge(Integer.parseInt(ageInput));
						updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber,
								3);
					} catch (NumberFormatException e) {
						System.out.println("�������� �Է°��� �ƴմϴ�.");
					}
				}
				break;
			case '4':
				System.out.print("������ ���� ������ �Է��ϼ���(yyyy-mm-dd)(�߰����� ���� �� Enter): ");
				String birthDateInput = sc.nextLine();
				if (!birthDateInput.isEmpty()) {
					phonebookVO.setBirthDate(birthDateInput);
					updateResult = phonebookDAO.updatePhoneNumber(phonebookVO, originalName, originalPhoneNumber, 4);
				}
				break;
			case 'q':
				return;
			default:
				System.out.println("�߸��� �Է°��Դϴ�.");
			}
			if (updateResult) {
				System.out.println("���� ���� �Ϸ�");
			} else {
				System.out.println("���� ���� ����");
			}
		}
	}
}
