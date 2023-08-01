package top.xclhove.spring.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xclhove.spring.service.impl.OneDriveItemServiceImpl;

@Configuration
@EnableScheduling
@Slf4j
public class OneDriveItemSchedule {
    @Autowired
    private OneDriveItemServiceImpl oneDriveItemService;

    /**
     * 项目启动 10 分钟后, 每 30 分钟执行一次刷新所有 OneDriveItem 的定时任务.
     */
    @Scheduled(fixedRate = 1000 * 60 * 30, initialDelay = 1000 * 60 * 10)
    void refreshAllOneDriveItem() {
        log.info("执行刷新所有 OneDriveItem 的定时任务");
        oneDriveItemService.getAllItemInfo();
    }
}
