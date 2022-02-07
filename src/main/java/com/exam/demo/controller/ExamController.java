package com.exam.demo.controller;

import com.exam.demo.entity.Exam;
import com.exam.demo.entity.Testpaper;
import com.exam.demo.entity.UserTestPaperScore;
import com.exam.demo.service.ExamService;
import com.exam.demo.service.ScoreService;
import com.exam.demo.service.TestPaperService;
import com.exam.demo.utils.WebResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.exam.demo.utils.WebResult.REQUEST_STATUS_SUCCESS;

@RestController
@RequestMapping("exam")
@Api(value="/exam",tags={"考试操作接口"})
public class ExamController {

    @Autowired
    private ExamService examService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private TestPaperService testPaperService;

    @GetMapping("findByTestPaperId")
    @ApiOperation(notes = "xiong",value = "根据试卷ID查询试卷的所有试题接口")
    public WebResult<Map<String, List<Object>>> findByTestPaperId(@RequestParam @ApiParam(name="testPaperId",required=true) Integer testPaperId) {
        return WebResult.<Map<String, List<Object>>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(examService.findByTestPaperId(testPaperId))
                .build();
    }

    @PostMapping("addProblem")
    @ApiOperation(notes = "xiong",value = "添加试卷试题接口")
    public WebResult<Integer> addProblem(@RequestBody @ApiParam(name="exam",required=true) Exam exam) {
        return WebResult.<Integer>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(examService.addProblem(exam))
                .build();
    }

    @DeleteMapping("deleteProblem/{id}")
    @ApiOperation(notes = "xiong",value = "删除试卷试题接口")
    public WebResult<Integer> deleteProblem(@PathVariable @ApiParam(name="id",required=true) Integer id) {
        return WebResult.<Integer>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(examService.deleteProblem(id))
                .build();
    }

    @GetMapping("findTesting")
    @ApiOperation(notes = "xiong",value = "查询正在考试试卷接口")
    public WebResult<List<Testpaper>> findTesting() {
        return WebResult.<List<Testpaper>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(testPaperService.findTesting())
                .build();
    }

    @GetMapping("findTested")
    @ApiOperation(notes = "xiong",value = "查询历史考试试卷接口")
    public WebResult<List<Testpaper>> findTested() {
        return WebResult.<List<Testpaper>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(testPaperService.findTested())
                .build();
    }

    @GetMapping("findNotStartTest")
    @ApiOperation(notes = "xiong",value = "查询尚未开始的考试试卷接口")
    public WebResult<List<Testpaper>> findNotStartTest() {
        return WebResult.<List<Testpaper>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(testPaperService.findNotStartTest())
                .build();
    }

    @GetMapping("findDetail")
    @ApiOperation(notes = "xiong",value = "查询考试成绩详情接口")
    public WebResult<List<UserTestPaperScore>> findDetail(@RequestParam @ApiParam(name="testPaperId",required=true) Integer testPaperId) {
        return WebResult.<List<UserTestPaperScore>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(scoreService.findByTestPaperId(testPaperId))
                .build();
    }

    @GetMapping("findByUserId")
    @ApiOperation(notes = "xiong",value = "查询用户考试成绩详情接口")
    public WebResult<List<UserTestPaperScore>> findByUserId(@RequestParam @ApiParam(name="userId",required=true) Integer userId) {
        return WebResult.<List<UserTestPaperScore>>builder()
                .code(200)
                .message(REQUEST_STATUS_SUCCESS)
                .data(scoreService.findByUserId(userId))
                .build();
    }
}