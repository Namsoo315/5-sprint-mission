import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mainview.MainViewBasic;
import com.sprint.mission.discodeit.mainview.MainViewFile;
import com.sprint.mission.discodeit.mainview.MainViewJCF;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {


	public static void main(String[] args) {
		MainViewJCF mainViewJCF = new MainViewJCF();
		MainViewFile mainViewFile = new MainViewFile();
		MainViewBasic mainViewBasic = new MainViewBasic();
		// mainViewJCF.mainMenu();		//JCF 테스트 용
		// mainViewFile.mainMenu(); 	//File 테스트 용
		mainViewBasic.mainMenuBasic();


	}
}
