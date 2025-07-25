import com.sprint.mission.discodeit.mainview.MainViewFile;
import com.sprint.mission.discodeit.mainview.MainViewJCF;


public class JavaApplication {
	public static void main(String[] args) {
		MainViewJCF mainViewJCF = new MainViewJCF();
		MainViewFile mainViewFile = new MainViewFile();
		// mainViewJCF.mainMenu();		//JCF 테스트 용
		mainViewFile.mainMenu(); 	//File 테스트 용
	}
}
