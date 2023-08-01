package top.xclhove.spring.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xclhove.spring.service.impl.OneDriveServiceImpl;

@Configuration
@EnableScheduling
@Slf4j
public class OneDriveSchedule {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    /**
     * 项目启动 10 分钟后, 每 5 分钟执行一次刷新 OneDrive access_token 的定时任务.
     */
    @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 60 * 10)
    private void refreshOneDriveAccessToken() {
        log.info("执行刷新 OneDrive access_token 的定时任务");
        oneDriveService.refreshAllOneDriveAccessToken();
    }
}
