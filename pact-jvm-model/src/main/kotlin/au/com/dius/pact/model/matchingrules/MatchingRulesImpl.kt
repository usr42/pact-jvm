package au.com.dius.pact.model.matchingrules

import au.com.dius.pact.model.PactSpecVersion

class MatchingRulesImpl : MatchingRules {

    val rules = mutableMapOf<String, Category>()

    override fun rulesForCategory(category: String): Category = addCategory(category)

    override fun addCategory(category: Category): Category {
        rules[category.name] = category
        return category
    }

    override fun addCategory(category: String): Category = rules.getOrPut(category, { Category(category) })

    fun copy(): MatchingRules {
        val copy = MatchingRulesImpl()
        rules.map { it.value }.forEach { copy.addCategory(it) }
        return copy
    }

    fun fromV2Map(map: Map<String, Map<String, Any?>>) {
        map.forEach {
            val path = it.key.split('.')
            if (it.key.startsWith("$.body")) {
                if (it.key == "$.body") {
                    addV2Rule("body", "$", it.value)
                } else {
                    addV2Rule("body", "$${it.key.substring(6)}", it.value)
                }
            } else if (it.key.startsWith("$.headers")) {
                addV2Rule("header", path[2], it.value)
            } else {
                addV2Rule(path[1], if (path.size > 2) path[2] else null, it.value)
            }
        }
    }

    fun isEmpty(): Boolean = rules.all { it.value.isEmpty() }

    fun isNotEmpty(): Boolean = !isEmpty()

    fun hasCategory(category: String): Boolean = rules.contains(category)

    fun getCategories(): Set<String> = rules.keys

    override fun toString(): String = "MatchingRules(rules=$rules)"
    override fun equals(other: Any?): Boolean = when (other) {
        is MatchingRulesImpl -> other.rules == rules
        else -> false
    }

    override fun hashCode(): Int = rules.hashCode()

    fun toMap(pactSpecVersion: PactSpecVersion): Map<String, Any?> = when {
        pactSpecVersion < PactSpecVersion.V3 -> toV2Map()
        else -> toV3Map()
    }

    private fun toV3Map(): Map<String, Map<String, Any?>> = rules.filter { it.value.isNotEmpty() }.mapValues { entry ->
        entry.value.toMap(PactSpecVersion.V3)
    }

    fun fromV3Map(map: Map<String, Map<String, Any?>>) {
        map.forEach {
            addRules(it.key, it.value)
        }
    }

    companion object {
        @JvmStatic
        fun fromMap(map: Map<String, Map<String, Any?>>?): MatchingRules {
            val matchingRules = MatchingRulesImpl()
            if (map != null && map.isNotEmpty()) {
                if (map.keys.first().startsWith("$")) {
                    matchingRules.fromV2Map(map)
                } else {
                    matchingRules.fromV3Map(map)
                }
            }
            return matchingRules
        }
    }

    private fun addRules(categoryName: String, matcherDef: Map<String, Any?>) {
        addCategory(categoryName).fromMap(matcherDef)
    }

    private fun toV2Map(): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        rules.forEach {
            it.value.toMap(PactSpecVersion.V2).forEach {
                result[it.key] = it.value
            }
        }
        return result
    }

    private fun addV2Rule(categoryName: String, item: String?, matcher: Map<String, Any?>) {
        val category = addCategory(categoryName)
        if (item != null) {
            category.addRule(item, MatchingRuleGroup.ruleFromMap(matcher))
        } else {
            category.addRule(MatchingRuleGroup.ruleFromMap(matcher))
        }
    }
}
