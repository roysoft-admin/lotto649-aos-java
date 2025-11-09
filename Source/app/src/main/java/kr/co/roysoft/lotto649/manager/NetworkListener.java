package kr.co.roysoft.lotto649.manager;

import kr.co.roysoft.lotto649.data.JsonDTO;

public interface NetworkListener {
	public void onComplete(JsonDTO dto);
	public void onError(JsonDTO dto);
	public void networkStart();
	public void networkEnd();
}
