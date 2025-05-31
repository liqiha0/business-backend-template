package io.github.liqiha0.backendtemplate.application.system

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class PcaCodeService(private val mapper: ObjectMapper) {
    private val pcaData: List<PcaItem>

    init {
        val resource = ClassPathResource("pca-code.json")
        pcaData = this.mapper.readValue(
            resource.inputStream,
            this.mapper.typeFactory.constructCollectionType(List::class.java, PcaItem::class.java)
        )
    }

    fun getNames(provinceCode: String, cityCode: String, districtCode: String): Triple<String, String, String> {
        val province = pcaData.find { it.code == provinceCode }
        val city = province?.children?.find { it.code == cityCode }
        val district = city?.children?.find { it.code == districtCode }

        return Triple(
            province?.name ?: "",
            city?.name ?: "",
            district?.name ?: ""
        )
    }

    data class PcaItem(
        val code: String,
        val name: String,
        val children: List<PcaItem>? = null
    )
}