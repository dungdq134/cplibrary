package pl.cyfrowypolsat.cpstats.common.appsflyer


data class AppsFlyerHit(val eventType: String,
                        val eventValues: HashMap<String, Any>)