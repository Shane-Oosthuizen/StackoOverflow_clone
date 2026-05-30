package com.shaneoosthuizen.assessment.clonedstackoverflow.components.searchcomponent.domain.models

enum class SearchTypeEnum (val value:String, val displayName: String){
    TAG("tag", "Tag"),
    PHRASE("phrase", "Phrase"),
    AUTHOR("author", "Author"),
    COLLECTIVE_NAME("collective_name", "Collective Name"),;

    override fun toString(): String {
        return value
    }
}