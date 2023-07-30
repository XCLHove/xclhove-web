package top.xclhove.spring.service.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xclhove.spring.service.impl.OneDriveServiceImpl;

@Configuration
@EnableScheduling
public class OneDriveSchedule {
    @Autowired
    private OneDriveServiceImpl oneDriveService;

    /**
     * 项目启动 10 秒后, 每 5 分钟执行一次刷新 OneDrive access_token 的定时任务.
     */
    @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 10)
    private void refreshOneDriveAccessToken() {
        oneDriveService.refreshOneDriveAccessToken();
    }
}
