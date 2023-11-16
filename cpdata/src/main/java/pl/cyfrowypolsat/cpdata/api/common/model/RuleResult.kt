package pl.cyfrowypolsat.cpdata.api.common.model

data class RuleResult(val id: Int,
                      val name: String,
                      val agreeDescription: String,
                      val description: String,
                      val type: String,
                      val version: Int,
                      val sources: List<RuleSourceResult>,
                      val required: Boolean = false,
                      val requiredAnswer: Boolean = false)

data class RuleSourceResult(val url: String,
                            val type: String)