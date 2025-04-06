package com.smarthealth.common.result;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult{

    private Long total; //总记录数

    private List records; //当前页数据集合
}