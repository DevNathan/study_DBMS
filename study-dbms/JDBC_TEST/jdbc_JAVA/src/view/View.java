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
			System.out.println("1. 로그인 | 2. 회원가입 | q. 시스템 종료");
			menuController = sc.next().charAt(0);

			switch (menuController) {
			case '1':
				view.login();
				break;
			case '2':
				view.join();
				break;
			case 'q':
				System.out.println("프로그램 종료");
				return;
			default:
				System.out.println("다시 입력하십시오.");
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
					"1. 게시판 | 2. 마이페이지 | 3. 회원 정보 수정 | 4. 추천으로 들어온 회원 조회 | " + "5. 내 추천인 보기 | 6. 로그아웃 | q. 프로그램 종료");
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
				System.out.println("시스템 종료");
				return;
			default:
				System.out.println("다시 입력하십시오.");
			}
			System.out.println();
		}
	}

	private static void printPostMenu() {
		ViewPost viewPost = new ViewPost();
		String menuController = "";
		while (true) {
			viewPost.viewPostsAll(); // 모든 포스트 출력
			System.out.println("게시글 ID를 입력시 게시글로 이동 | w. 게시글 작성 | q. 뒤로가기");
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
				System.out.println("올바른 메뉴를 선택해주세요.");
			}
			System.out.println();
		}
	}

	private void login() {
		MemberDAO memberDAO = new MemberDAO();
		MemberVO memberVO = new MemberVO();
		String id = "", password = "";

		System.out.print("아이디를 입력해주세요 : ");
		id = sc.next();
		memberVO.setMemberId(id);
		System.out.print("비밀번호를 입력해주세요 : ");
		password = sc.next();
		memberVO.setMemberPassword(password);

		if (memberDAO.login(memberVO)) {
			System.out.println("환영합니다.");
		} else {
			System.out.println("아이디와 비밀번호가 일치하지 않습니다.");
		}
	}

	private void logout() {
		MemberDAO memberDAO = new MemberDAO();
		if (memberDAO.session == null) {
			System.out.println("이미 로그아웃 되어있습니다.");
			return;
		}
		memberDAO.logout();
		if (memberDAO.session == null) {
			System.out.println("로그아웃되었습니다.");
		} else {
			System.out.println("로그아웃 실패");
		}
	}

	private void join() {
		MemberDAO memberDAO = new MemberDAO();
		MemberVO memberVO = new MemberVO();
		String id = "", password = "", name = "", address = "";

		System.out.print("아이디를 입력해주세요 : ");
		id = sc.next();
		if (memberDAO.checkId(id)) {
			System.out.println("사용 가능한 아이디");
			memberVO.setMemberId(id);
		} else {
			System.out.println("중복된 아이디 입니다.\n");
			return;
		}
		System.out.print("비밀번호를 입력해주세요 : ");
		password = sc.next();
		memberVO.setMemberPassword(password);
		sc.nextLine(); // 개행 문자 처리
		System.out.print("실명 입력 : ");
		name = sc.nextLine();
		memberVO.setMemberName(name);
		System.out.print("주소 입력 : ");
		address = sc.nextLine();
		memberVO.setMemberAddress(address);
		memberDAO.join(memberVO);
	}

	private void mypage() {
		MemberDAO memberDAO = new MemberDAO();
		Long id;

		if (memberDAO.session == null) {
			System.out.println("먼저 로그인을 해주세요.");
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
			System.out.println("먼저 로그인을 해주세요.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		System.out.println("어떤 정보를 수정하시겠습니까?");
		System.out.print("1. 이름, 2. 비밀번호, 3. 주소 : ");
		select = sc.nextInt();
		if (select > 3 || select == 0) {
			System.out.println("잘못 입력하셨습니다.");
			return;
		}
		sc.nextLine(); // 개행 문자 처리
		System.out.print("변경할 내용을 입력해주세요. : ");
		stringToUpdate = sc.nextLine();

		memberDAO.update(select, stringToUpdate);
	}

	private void delete() {
		MemberDAO memberDAO = new MemberDAO();
		char select;

		if (memberDAO.session == null) {
			System.out.println("먼저 로그인을 해주세요.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		while (true) {
			System.out.println("정말 계정을 삭제 하시겠습니까?");
			System.out.println("예 : y / 아니요 : n");
			char input = sc.next().charAt(0);
			if (input == 'y' || input == 'Y') {
				memberDAO.deleteAccount();
				break;
			} else if (input == 'n' || input == 'N') {
				return;
			} else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
		}
	}

	private void recommendedUser() {
		MemberDAO memberDAO = new MemberDAO();

		if (memberDAO.session == null) {
			System.out.println("먼저 로그인을 해주세요.");
			view.login();
			if (memberDAO.session == null) {
				return;
			}
		}
		Object[] result = memberDAO.recommendCount();

		int count = (int) result[0];
		String[] names = (String[]) result[1];
		if (count == 0) {
			System.out.println("귀하의 추천을 받은 회원이 없습니다.");
		} else {
			System.out.println(count + "명이 나의 추천을 받아 가입했습니다.");

			System.out.println("-명단-");
			for (int i = 0; i < count; i++) {
				System.out.println(names[i]);
			}
		}
	}

	private void myRecommender() {
		MemberDAO memberDAO = new MemberDAO();

		if (memberDAO.session == null) {
			System.out.println("먼저 로그인을 해주세요.");
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
		System.out.println("추천인이 없습니다.");
	}
}
