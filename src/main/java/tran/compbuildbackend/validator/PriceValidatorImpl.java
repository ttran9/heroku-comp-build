package tran.compbuildbackend.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import tran.compbuildbackend.domain.computerbuild.ComputerPart;

import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.INVALID_PRICE_FORMAT_KEY;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.PRICE_OUT_OF_RANGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.PRICE_FIELD;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.PRICE_INCORRECT_FORMAT;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.PRICE_INCORRECT_RANGE;

@Component
public class PriceValidatorImpl implements PriceValidator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ComputerPart.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ComputerPart computerPart = (ComputerPart) o;
        double price = computerPart.getPrice();
        double minPrice = 0;
        double maxPrice = 1000000;
        String priceAsStr = String.valueOf(price);
        String priceRegex ="^\\d{1,6}\\.\\d{0,2}$";

        // check if the price follows a certain format.
        if(!priceAsStr.matches(priceRegex)) {
            errors.rejectValue(PRICE_FIELD, INVALID_PRICE_FORMAT_KEY, PRICE_INCORRECT_FORMAT);
        }

        // check if the price is within a certain range.
        if(price < minPrice || price > maxPrice) {
            errors.rejectValue(PRICE_FIELD, PRICE_OUT_OF_RANGE_KEY, PRICE_INCORRECT_RANGE);
        }
    }
}
