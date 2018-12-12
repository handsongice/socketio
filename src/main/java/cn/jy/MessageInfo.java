package cn.jy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: jason ji
 * @Date: 2018/12/12 9:06
 */
@Getter
@Setter
@ToString
public class MessageInfo {

    String msgContent;

    String msgType;

    String sourceClientId;

    String targetClientId;

}
