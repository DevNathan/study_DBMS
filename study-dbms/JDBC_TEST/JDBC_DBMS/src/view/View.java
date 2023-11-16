package view;

import java.util.Scanner;

import dao.MemberDAO;
import dao.PostDAO;
import dao.ReplyDAO;
import vo.MemberVO;
import vo.ReplyVO;

public class View {
	static View view = new View();
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		printMenu1();
	}

	private static void printMenu1() {
		char menuController = 0;

		while (MemberDAO.session == null) {
			System.out.println("1. �α��� | 2. ȸ������ | q. �ý��� ����");
			menuController = sc.next().charAt(0);

			switch (menuController) {
			case '1':
				view.login();
				break;
			case '2':
				view.join();
				break;
			case 'q':
				System.out.println("���α׷� ����");
				return;
			default:
				System.out.println("�ٽ� �Է��Ͻʽÿ�.");
			}
			System.out.println();
			if (MemberDAO.session != null)
				printMenu2();
		}
	}

	private static void printMenu2() {
		char menuController = 0;
		while (MemberDAO.session != null) {
			System.out.println(
					"1. �Խ��� | 2. ���������� | 3. ȸ�� ���� ���� | 4. ��õ���� ���� ȸ�� ��ȸ | " + "5. �� ��õ�� ���� | 6. �α׾ƿ� | q. ���α׷� ����");
			menuController = sc.next().charAt(0);

			switch (menuController) {
			case '1':
				view.printPostMenu();
				break;
			case '2':
				view.mypage();
				break;
			case '3':
				view.update();
				break;
			case '4':
				view.recommendedUser();
				break;
			case '5':
				view.myRecommender();
				break;
			case '6':
				view.logout();
				break;
			case 'q':
				System.out.println("�ý��� ����");
				return;
			default:
				System.out.println("�ٽ� �Է��Ͻʽÿ�.");
			}
			System.out.println();
		}
	}

	private static void printPostMenu() {
		ViewPost viewPost = new ViewPost();
		String menuController = "";
		while (true) {
			viewPost.viewPostsAll(); // ��� ����Ʈ ���
			System.out.println("�Խñ� ID�� �Է½� �Խñ۷� �̵� | w. �Խñ� �ۼ� | q. �ڷΰ���");
			menuController = sc.next();
			sc.nextLine();

			if (Character.isDigit(menuController.charAt(0))) {
				Long postId = Long.valueOf(menuController);
				PostDAO.postSession = postId;
				viewPost.viewPost(postId);
			} else if (menuController.equals("w")) {
				viewPost.createPost();
			} else if (menuController.equals("q")) {
				return;
			} else {
				System.out.println("�ùٸ� �޴��� �������ּ���.");
			}
			System.out.println();
		}
	}

	private void login() {
		MemberDAO memberDAO = new MemberDAO();
		MemberVO memberVO = new MemberVO();
		String id = "", password = "";

		System.out.print("���̵� �Է����ּ��� : ");
		id = sc.next();
		memberVO.setMemberId(id);
		System.out.print("��й�ȣ�� �Է����ּ��� : ");
		password = sc.next();
		memberVO.setMemberPassword(password);

		if (memberDAO.login(memberVO)) {
			System.out.println("ȯ���մϴ�.");
		} else {
			System.out.println("���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
		}
	}

	private void logout() {
		MemberDAO memberDAO = new MemberDAO();
		if (memberDAO.session == null) {
			System.out.println("�̹� �α׾ƿ� �Ǿ��ֽ��ϴ�.");
			return;
		}
		memberDAO.logout();
		if (memberDAO.session == null) {
			System.out.println("�α׾ƿ��Ǿ����ϴ�.");
		} else {
			System.out.println("�α׾ƿ� ����");
		}
	}

	private void join() {
		MemberDAO memberDAO = new MemberDAO();
		MemberVO memberVO = new MemberVO();
		String id = "", password = "", name = "", address = "";

		System.out.print("���̵� �Է����ּ��� : ");
		id = sc.next();
		if (memberDAO.checkId(id)) {
			System.out.println("��� ������ ���̵�");
			memberVO.setMemberId(id);
		} else {
			System.out.println("�ߺ��� ���̵� �Դϴ�.\n");
			return;
		}
		System.out.print("��й�ȣ�� �Է����ּ��� : ");
		password = sc.next();
		memberVO.setMemberPassword(password);
		sc.nextLine(); // ���� ���� ó��
		System.out.print("�Ǹ� �Է� : ");
		name = sc.nextLine();
		memberVO.setMemberName(name);
		System.out.print("�ּ� �Է� : ");
		address = sc.nextLine();
		memberVO.setMemberAddress(address);
		memberDAO.join(memberVO);
	}

	private void mypage() {
		MemberDAO memberDAO = new MemberDAO();
		Long id;

		if (memberDAO.session == null) {
			System.out.println("���� �α����� ���ּ���.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		System.out.println(memberDAO.findById());
	}

	private void update() {
		MemberDAO memberDAO = new MemberDAO();
		int select = 0;
		String stringToUpdate = "";

		if (memberDAO.session == null) {
			System.out.println("���� �α����� ���ּ���.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		System.out.println("� ������ �����Ͻðڽ��ϱ�?");
		System.out.print("1. �̸�, 2. ��й�ȣ, 3. �ּ� : ");
		select = sc.nextInt();
		if (select > 3 || select == 0) {
			System.out.println("�߸� �Է��ϼ̽��ϴ�.");
			return;
		}
		sc.nextLine(); // ���� ���� ó��
		System.out.print("������ ������ �Է����ּ���. : ");
		stringToUpdate = sc.nextLine();

		memberDAO.update(select, stringToUpdate);
	}

	private void delete() {
		MemberDAO memberDAO = new MemberDAO();
		char select;

		if (memberDAO.session == null) {
			System.out.println("���� �α����� ���ּ���.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		while (true) {
			System.out.println("���� ������ ���� �Ͻðڽ��ϱ�?");
			System.out.println("�� : y / �ƴϿ� : n");
			char input = sc.next().charAt(0);
			if (input == 'y' || input == 'Y') {
				memberDAO.deleteAccount();
				break;
			} else if (input == 'n' || input == 'N') {
				return;
			} else {
				System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
			}
		}
	}

	private void recommendedUser() {
		MemberDAO memberDAO = new MemberDAO();

		if (memberDAO.session == null) {
			System.out.println("���� �α����� ���ּ���.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		Object[] result = memberDAO.recommendCount();

		int count = (int) result[0];
		String[] names = (String[]) result[1];
		if (count == 0) {
			System.out.println("������ ��õ�� ���� ȸ���� �����ϴ�.");
		} else {
			System.out.println(count + "���� ���� ��õ�� �޾� �����߽��ϴ�.");

			System.out.println("-���-");
			for (int i = 0; i < count; i++) {
				System.out.println(names[i]);
			}
		}
	}

	private void myRecommender() {
		MemberDAO memberDAO = new MemberDAO();

		if (memberDAO.session == null) {
			System.out.println("���� �α����� ���ּ���.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		String result = memberDAO.viewMyRecommender();
		if (result != null) {
			System.out.println(result);
			return;
		}
		System.out.println("��õ���� �����ϴ�.");
	}
}
