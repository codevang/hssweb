package control;

import java.io.IOException;

public interface Command {
	
	// 기능을 수행하는 클래스가 꼭 구현해야할 메소드
	abstract public int execute();
	
}
