package com.exam.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.demo.entity.ExamJudge;
import com.exam.demo.entity.ExamSelect;
import com.exam.demo.mapper.ExamJudgeMapper;
import com.exam.demo.mapper.SubjectMapper;
import com.exam.demo.results.vo.ExamJudgeVo;
import com.exam.demo.results.vo.PageVo;
import com.exam.demo.service.ExamJudgeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ExamJudgeServiceImpl implements ExamJudgeService {

    @Autowired
    private ExamJudgeMapper examJudgeMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public List<ExamJudge> findAll() {
        return examJudgeMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<ExamJudge> findPage(int current, int pageSize) {
        Page<ExamJudge> page = new Page<>(current, pageSize);
        Page<ExamJudge> examJudgePage = examJudgeMapper.selectPage(page, new LambdaQueryWrapper<>());
        return examJudgePage.getRecords();
    }

    @Override
    public ExamJudge findById(Integer id) {
        return examJudgeMapper.selectById(id);
    }

    @Override
    public List<ExamJudge> findBySubjectId(Integer subjectId) {
        QueryWrapper<ExamJudge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id",subjectId);
        return examJudgeMapper.selectList(queryWrapper);
    }

    @Override
    public PageVo<ExamJudgeVo> search(Integer current, Integer pageSize, Integer id, String context) {
        Page<ExamJudge> page = new Page(current, pageSize);
        LambdaQueryWrapper<ExamJudge> queryWrapper = Wrappers.lambdaQuery(ExamJudge.class);

        if (id != null) {
            queryWrapper.eq(ExamJudge::getId, id);
        }
        if (!StringUtils.isBlank(context)) {
            queryWrapper.like(ExamJudge::getContext, context);
        }
        Page<ExamJudge> selectPage = examJudgeMapper.selectPage(page, queryWrapper);
        LinkedList<ExamJudgeVo> examJudgeVos = new LinkedList<>();
        for (ExamJudge record : selectPage.getRecords()) {
            ExamJudgeVo examJudgeVo = copy(new ExamJudgeVo(), record);
            examJudgeVos.add(examJudgeVo);
        }
        return PageVo.<ExamJudgeVo>builder()
                .values(examJudgeVos)
                .size(pageSize)
                .page(current)
                .total(selectPage.getTotal())
                .build();
    }

    private ExamJudgeVo copy(ExamJudgeVo examJudgeVo, ExamJudge examJudge) {
        BeanUtils.copyProperties(examJudge,examJudgeVo);
        examJudgeVo.setSubject(subjectMapper.selectById(examJudge.getSubjectId()).getName());
        return examJudgeVo;
    }

    @Override
    public Integer saveExamJudge(ExamJudge examJudge) {
        return examJudgeMapper.insert(examJudge);
    }

    @Override
    public Integer updateExamJudge(ExamJudge examJudge) {
        return examJudgeMapper.updateById(examJudge);
    }

    @Override
    public Integer deleteExamJudge(Integer id) {
        return examJudgeMapper.deleteById(id);
    }
}
