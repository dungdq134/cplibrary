package pl.cyfrowypolsat.cpdata;

import android.content.Context;

import androidx.annotation.NonNull;

import pl.cyfrowypolsat.cpdata.api.common.session.AutoLoginFailedListener;
import pl.cyfrowypolsat.cpdata.api.common.useragent.UserAgentDataBuilder;
import pl.cyfrowypolsat.cpdata.common.ApplicationData;
import pl.cyfrowypolsat.cpdata.di.CpDataComponent;
import pl.cyfrowypolsat.cpdata.di.DaggerCpDataComponent;

public class CpData {

    private CpData() {

    }

    private static CpData instance;

    public static CpData getInstance() {
        if (instance == null) {
            instance = new CpData();
        }
        return instance;
    }

    private CpDataComponent cpDataComponent;

    /**
     * Should be called in Application onCreate.
     */
    public void init(@NonNull Context context,
                     @NonNull String portalName,
                     @NonNull Integer buildVersion,
                     @NonNull Boolean isAndroidTv,
                     @NonNull Boolean isAmazonDevice,
                     @NonNull Boolean isAmazonFireTv,
                     @NonNull Boolean isTMobileSTB,
                     @NonNull Boolean isToyaSTB,
                     @NonNull Boolean isPolsatOrNetiaSTB,
                     @NonNull Boolean hasGoogleServices,
                     @NonNull Boolean isProduction,
                     @NonNull AutoLoginFailedListener autoLoginFailedListener) {
        UserAgentDataBuilder userAgentDataBuilder = new UserAgentDataBuilder(portalName,
                buildVersion,
                isAndroidTv,
                isAmazonDevice,
                isAmazonFireTv,
                isTMobileSTB,
                isToyaSTB,
                isPolsatOrNetiaSTB,
                hasGoogleServices);
        ApplicationData applicationData = new ApplicationData(isProduction);
        initComponent(context, userAgentDataBuilder, applicationData, autoLoginFailedListener);
    }

    public CpDataComponent getComponent() {
        if (cpDataComponent == null) {
            throw new RuntimeException("CpData is not initialize correctly. Call CpData.getInstance().init() in Application onCreate.");
        }
        return cpDataComponent;
    }

    private void initComponent(Context context,
                               UserAgentDataBuilder userAgentDataBuilder,
                               ApplicationData applicationData,
                               AutoLoginFailedListener listener) {
        cpDataComponent = DaggerCpDataComponent
                .builder()
                .context(context)
                .userAgentDataBuilder(userAgentDataBuilder)
                .applicationData(applicationData)
                .autoLoginFailedListener(listener)
                .build();
    }

}
