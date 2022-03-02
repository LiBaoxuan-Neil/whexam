package com.exam.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "examSelect")
public class ExamSelect {

    @ApiModelProperty(value = "选择题主键")
    private int id;

    @ApiModelProperty(value = "题目内容")
    private String context;

    @ApiModelProperty(value = "选项内容")
    private String selection;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "所属科目ID")
    private Integer subjectId;

    @ApiModelProperty(value = "难度")
    private Integer difficulty;

    @ApiModelProperty(value = "分数")
    private Double score;

    @ApiModelProperty(value = "选择题目类型")
    private Integer type;

    @ApiModelProperty(value = "图片URL")
    private String imgUrl;
}
