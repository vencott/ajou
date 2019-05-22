package com.f041.team4.global;

public class Constants {
    public static final int REGISTER = 1000;
    public static final int SESSION = 1001;

    public static final String INIT_MESSAGE = "제가 생성한 Session Key를 당신의 Public Key로 암호화하여 전송하였습니다.";
    public static final String REPLY_MESSAGE = "당신이 저의 Public Key로 암호화하여 보낸 Session Key를 제 Private Key로 복호화하였습니다.\n이제 저희 둘다 Session Key를 가지게 되었으니, 이 Session key를 이용해 암호화된 메시지를 주고받고, 각자의 Sessoin Key로 이를 복호화 해 커뮤니케이션을 할 수 있습니다.";
}
