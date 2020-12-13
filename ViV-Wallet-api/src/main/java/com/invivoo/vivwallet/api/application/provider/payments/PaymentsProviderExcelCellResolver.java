package com.invivoo.vivwallet.api.application.provider.payments;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Optional;
import java.util.function.Function;

public enum PaymentsProviderExcelCellResolver {
    FULL_NAME(0, Cell::getStringCellValue),
    VIV_AMOUNT(1, Cell::getNumericCellValue),
    DATE(2, Cell::getDateCellValue);

    private final Integer cellNum;
    private final Function<Cell, ?> resolver;

    <T> PaymentsProviderExcelCellResolver(Integer cellNum, Function<Cell, T> resolver) {
        this.cellNum = cellNum;
        this.resolver = resolver;
    }

    public <T> T resolve(Row row) {
        return (T) Optional.ofNullable(row.getCell(cellNum))
                           .map(resolver::apply)
                           .orElse(null);
    }
}
