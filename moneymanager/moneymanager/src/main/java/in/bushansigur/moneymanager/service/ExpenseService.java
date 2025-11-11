package in.bushansigur.moneymanager.service;

import in.bushansigur.moneymanager.dto.ExpenseDTO;
import in.bushansigur.moneymanager.entity.CategoryEntity;
import in.bushansigur.moneymanager.entity.ExpenseEntity;
import in.bushansigur.moneymanager.entity.ProfileEntity;
import in.bushansigur.moneymanager.repository.CategoryRepository;
import in.bushansigur.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        ExpenseEntity newExpense = toEntity(expenseDTO, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }
    //handel method
    private ExpenseEntity toEntity(ExpenseDTO dTO, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .name(dTO.getName())
                .amount(dTO.getAmount())
                .category(category)
                .profile(profile)
                .icon(dTO.getIcon())
                .build();
    }
    public ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
                .name(entity.getName())
                .amount(entity.getAmount())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId(): null)
                .categoryName(entity.getCategory() != null? entity.getCategory().getName(): "N/A")
                .build();
    }
}
