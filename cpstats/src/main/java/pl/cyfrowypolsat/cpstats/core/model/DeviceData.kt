package pl.cyfrowypolsat.cpstats.core.model

data class DeviceData(val deviceType: DeviceType,
                      val deviceIdValue: String,
                      val deviceIdType: String,
                      val platform: Platform,
                      val manufacturer: String,
                      val model: String,
                      val os: String,
                      val osInfo: String,
                      val screenHeight: Int,
                      val screenWidth: Int,
                      val screenDiagonal: Double,
                      val hardwareInfo: String,
                      val deviceVolumeLevel: Int)