package com.demo.crawler.task;

import com.demo.javademo.dto.HouseDTO;
import com.demo.javademo.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
@Component
public class HouseDataPipeline implements Pipeline {

    @Autowired
    private HouseService houseService;
    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的租房详情对象
        HouseDTO houseInfo = resultItems.get("houseInfo");
        //判断数据是否不为空
        if (houseInfo != null){
            houseService.create(houseInfo);
        }
    }
}
