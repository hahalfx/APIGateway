package com.example.demo.entity;

import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.Map;




public class MessageTask {
    // 添加常量，定义正则表达式
    public static final String REGEX_MOBILE = "^\\d{11}$";
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";



    private String imessageId; // 任务ID
    private String taskName; // 任务名
    private String deliveryChannel; // 发送渠道：1-站内消息，2-邮件，3-手机号
    private String content; // 正文内容
    private String  senderRecipient; // 发送方信息，例如手机号、邮箱、站内ID等
    private String receiverInformation; // 接收者信息，包含手机号、邮箱等
    private LocalDateTime sendTime; // 计划发送时间
    private LocalDateTime actualSendTime; // 实际发送时间
    private String status; // 消息发送状态，如"pending", "sent", "failed"
    private LocalDateTime createdAt; // 任务创建时间
    private LocalDateTime updatedAt; // 任务最后更新时间
    private boolean alive; // 是否有效

    // getter和setter方法
    // Getter and Setter methods
    public String getImessageId() { return imessageId; }
    public void setImessageId(String imessageId) { this.imessageId = imessageId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDeliveryChannel() { return deliveryChannel; }
    public void setDeliveryChannel(String deliveryChannel) { this.deliveryChannel = deliveryChannel; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSenderRecipient() { return senderRecipient; }
    public void setSenderRecipient(String senderRecipient) { this.senderRecipient = senderRecipient; }

    public String getReceiverInformation() { return receiverInformation; }
    public void setReceiverInformation(String receiverInformation) { this.receiverInformation = receiverInformation; }

    public LocalDateTime getSendTime() { return sendTime; }
    public void setSendTime(LocalDateTime sendTime) { this.sendTime = sendTime; }

    public LocalDateTime getActualSendTime() { return actualSendTime; }
    public void setActualSendTime(LocalDateTime actualSendTime) { this.actualSendTime = actualSendTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    // 构造方法，可以根据需要提供不同的构造方式
    public MessageTask() {
        // 默认构造方法
    }

    // 可以有带参数的构造方法，便于初始化对象
    public MessageTask(String imessageId, String content, String deliveryChannel,
                      String senderRecipient, String receiverInformation,
                       LocalDateTime sendTime) {
        this.imessageId = imessageId;
        this.content = content;
        this.deliveryChannel = deliveryChannel;
        this.senderRecipient = senderRecipient;
        this.receiverInformation = receiverInformation;
        this.sendTime = sendTime;
        this.status = "pending"; // 初始化状态为待发送
        this.createdAt = LocalDateTime.now(); // 记录创建时间
        this.alive = true; // 默认是有效的
    }
public  boolean isHavercvr(){
        if(this.receiverInformation.equals(null)){
            return    false;
        }
        return  true;
}
    // 实现消息发送逻辑的接口调用方法
    public void sendMessage() {
        if ("1".equals(deliveryChannel)) {
            // 调用站内消息服务
            // 假设有一个MessageACTService实例可以调用
            // MessageACTService.sendToIntranet(this);
        } else if ("2".equals(deliveryChannel)) {
            // 调用邮件服务
            // MailService.send(this);
        } else if ("3".equals(deliveryChannel)) {
            // 调用短信服务
            // SMSService.send(this);
        } else {
            throw new IllegalArgumentException("Unsupported delivery channel: " + deliveryChannel);
        }
    }

    // 该方法可以用于在Controller层验证并填充非必要字段
    public void validateAndFillDefaults() {
        // 检查必要字段是否齐全，比如imessageId, content, deliveryChannel, sendTime
        // 如果某些非必要字段为空，可以在这里进行默认值填充
        if (this.sendTime == null) {
            this.sendTime = LocalDateTime.now(); // 如果发送时间未设定，默认立即发送
        }
        // 其他逻辑...
    }



    public boolean isReceiverInfoValid() {
        if ("3".equals(deliveryChannel)) { // 判断是否为手机号
            return isValidMobile(receiverInformation);
        } else if ("2".equals(deliveryChannel)) { // 判断是否为邮箱
            return isValidEmail(receiverInformation);
        } else if ("1".equals(deliveryChannel)) { // 对于站内消息，可能不需要验证
            return true; // 假设站内ID总是有效的
        } else {
            throw new IllegalArgumentException("Unsupported delivery channel: " + deliveryChannel);
        }
    }

    private boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        return pattern.matcher(mobile).matches();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        return pattern.matcher(email).matches();
    }

}




