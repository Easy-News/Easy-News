package shimp.easy_news.news.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SubCategoryConverter implements AttributeConverter<SubCategory, String> {

    @Override
    public String convertToDatabaseColumn(SubCategory attribute) {
        return attribute.name();
    }

    @Override
    public SubCategory convertToEntityAttribute(String dbData) {
        return SubCategory.valueOf(dbData.trim());
    }
}