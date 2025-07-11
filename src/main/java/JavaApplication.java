import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
	public static void main(String[] args) {
		UserService userService = new JCFUserService();

		userCRUDTest(userService);
	}

	private static void userCRUDTest(UserService userService) {
		//등록
		System.out.println("===== User 생성 =====");
		User user = userService.createUser();
		System.out.println("User 생성 : " + user.getId());
		System.out.println();

		//조회(단건)
		System.out.println("===== User 하나만 조회 =====");
		Optional<User> byId = userService.findById(user.getId());
		byId.ifPresent(u -> System.out.println("유저 하나 조회 : " + u.getId()));
		System.out.println();

		//조회(다건)
		List<User> users = userService.findByAll();
		System.out.println("===== User 전부 조회 =====");
		users
			.stream()
			.sorted(Comparator.comparing(User::getUsername))
			.forEach(u -> System.out.println(
			"유저 : " + u.getId() + "| 이름 : " + u.getUsername() + " | 나이 : " + u.getAge() + " | 생성 날짜 : "
				+ u.getCreatedAt() + " | 업데이트 날짜 : " + u.getUpdatedAt()));
		System.out.println();

		//수정
		System.out.println("===== User 수정 =====");
		System.out.println("수정되는 UUID : " + user.getId());
		System.out.println();
		String updateName = "수정된 이름";
		int updateAge = 100;
		userService.updateUser(user.getId(), updateName, updateAge);        //수정 시작

		//수정된 데이터 조회
		System.out.println("===== User 수정 조회=====");
		System.out.println(user);
		System.out.println();

		//수정 후 전체 데이터 조회도
		System.out.println("===== User 수정후 전체 조회=====");
		users
			.stream()
			.sorted(Comparator.comparing(User::getUsername))
			.forEach(u -> System.out.println(
			"유저 : " + u.getId() + "| 이름 : " + u.getUsername() + " | 나이 : " + u.getAge() + " | 생성 날짜 : "
				+ u.getCreatedAt() + " | 업데이트 날짜 : " + u.getUpdatedAt()));
		System.out.println();

		//삭제
		System.out.println("===== User 삭제=====");
		System.out.println("삭제할 UUID : " + user.getId());
		userService.deleteUser(user.getId());
		System.out.println();

		//조회를 통해 삭제되었는지 확인
		System.out.println("===== User 삭제 조회=====");
		Optional<User> byDeletedId = userService.findById(user.getId());

		if (byDeletedId.isEmpty()) {
			System.out.println("삭제 되었습니다.");
		} else {
			System.out.println("삭제되지 않았습니다.");
		}

	}
}
