package com.invivoo.vivwallet.api.application.provider.user;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Optional;
import java.util.function.Function;

public enum UsersProviderExcelCellResolver {
    FULL_NAME(1, Cell::getStringCellValue),
    EXPERTISE_1(2, Cell::getStringCellValue),
    EXPERTISE_2(3, Cell::getStringCellValue),
    EXPERTISE_STATUS(4, Cell::getStringCellValue),
    ROLE_TYPE(5, Cell::getStringCellValue),
    GT_ENTRY_DATE(6, Cell::getDateCellValue);

    private final Integer cellNum;
    private final Function<Cell, ?> resolver;

    <T> UsersProviderExcelCellResolver(Integer cellNum, Function<Cell, T> resolver) {
        this.cellNum = cellNum;
        this.resolver = resolver;
    }

    public <T> T resolve(Row row) {
        return (T) Optional.ofNullable(row.getCell(cellNum))
                           .map(resolver::apply)
                           .orElse(null);
    }
}
