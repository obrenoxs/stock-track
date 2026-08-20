package com.stocktrack.loan.mapper;

import com.stocktrack.loan.dto.request.LoanCreateRequestDTO;
import com.stocktrack.loan.dto.response.LoanResponseDTO;
import com.stocktrack.loan.entity.Loan;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LoanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "observation", ignore = true)
    @Mapping(target = "loanDate", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "tool", ignore = true)
    @Mapping(target = "borrowedByUser", ignore = true)
    @Mapping(target = "returnedByUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Loan toEntity(LoanCreateRequestDTO dto);

    @Mapping(target = "late", source = "late")
    LoanResponseDTO toResponseDTO(Loan loan, boolean late);

    @Mapping(target = "toolTypeName", source = "toolType.name")
    LoanResponseDTO.ToolSummaryDTO toToolSummaryDTO(Tool tool);

    LoanResponseDTO.UserSummaryDTO toUserSummaryDTO(User user);
}
