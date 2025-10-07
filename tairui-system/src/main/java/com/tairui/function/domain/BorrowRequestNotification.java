package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import jakarta.validation.constraints.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 借用申请提醒消息模板实体
 */
@Data
public class BorrowRequestNotification {
    public String templateId; // 模板id
    public String templateNumber; // 模板编号

    public String title; // 标题
    public String category; // 类目
    @NotBlank(message = "申请用户名称不能为空")
    public String applyUserName; //申请用户名称
    @NotBlank(message = "设备名称不能为空")
    public String deviceName; // 钥匙名称
    @NotBlank(message = "用途不能为空")
    public String purpose; // 用途
    @NotNull(message = "使用时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date useTime; // 使用时间
    @NotBlank(message = "备注不能为空")
    public String comments; // 审批说明
    @NotBlank(message = "openId不能为空")
    public String openId; // 微信用户openId

    public String miniprogramState; // 版本

    public String buildMessageJson() throws JsonProcessingException {
        Map<String, Object> msg = new HashMap<>();
        msg.put("touser", getOpenId());
        msg.put("template_id", this.getTemplateId());
        msg.put("miniprogram_state", getMiniprogramState());
        Map<String, Object> data = new HashMap<>();

        Map<String, String> shortThing1 = new HashMap<>();
        shortThing1.put("value", this.getApplyUserName());
        data.put("short_thing1", shortThing1);

        Map<String, String> thing2 = new HashMap<>();
        thing2.put("value", this.getDeviceName());
        data.put("thing2", thing2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, String> time3 = new HashMap<>();
        time3.put("value", sdf.format(this.getUseTime()));
        data.put("time3", time3);

        Map<String, String> thing4 = new HashMap<>();
        thing4.put("value", getPurpose());
        data.put("thing4", thing4);

        Map<String, String> thing5 = new HashMap<>();
        thing5.put("value", getComments());
        data.put("thing5", thing5);

        msg.put("data", data);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(msg);
        return json;
    }
}
