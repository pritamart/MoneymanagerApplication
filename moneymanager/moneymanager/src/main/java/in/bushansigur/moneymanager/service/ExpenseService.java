package in.bushansigur.moneymanager.service;

import in.bushansigur.moneymanager.dto.ExpenseDTO;
import in.bushansigur.moneymanager.entity.CategoryEntity;
import in.bushansigur.moneymanager.entity.ExpenseEntity;
import in.bushansigur.moneymanager.entity.ProfileEntity;
import in.bushansigur.moneymanager.repository.CategoryRepository;
import in.bushansigur.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<ExpenseDTO> getCurrentMonthExpenseForUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate  now = LocalDate.now();
        LocalDateTime startDate = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream()
                .map(this::toDTO)
                .toList();
    }

    public void  deleteExpense(Long expenseId){
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(expenseId).orElseThrow(()-> new RuntimeException("Expense not found"));
        if(!profile.getId().equals(expense.getProfile().getId())) {
            throw new RuntimeException("Unauthorized to delete expense");
        }
        expenseRepository.delete(expense);
    }



    //handel method
    private ExpenseEntity toEntity(ExpenseDTO dTO, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .id(dTO.getId())
                .name(dTO.getName())
                .amount(dTO.getAmount())
                .category(category)
                .profile(profile)
                .icon(dTO.getIcon())
                .build();
    }
    public ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId(): null)
                .categoryName(entity.getCategory() != null? entity.getCategory().getName(): "N/A")
                .build();
    }
}
