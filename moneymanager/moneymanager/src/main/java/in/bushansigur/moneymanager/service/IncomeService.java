package in.bushansigur.moneymanager.service;

import in.bushansigur.moneymanager.dto.IncomeDTO;
import in.bushansigur.moneymanager.entity.CategoryEntity;
import in.bushansigur.moneymanager.entity.IncomeEntity;
import in.bushansigur.moneymanager.entity.ProfileEntity;
import in.bushansigur.moneymanager.repository.CategoryRepository;
import in.bushansigur.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        IncomeEntity newIncome = toEntity(incomeDTO, profile, category);
        newIncome = incomeRepository.save(newIncome);
        return toDTO(newIncome);

    }




    //handel method
    private IncomeEntity toEntity(IncomeDTO dTO, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .name(dTO.getName())
                .amount(dTO.getAmount())
                .category(category)
                .profile(profile)
                .icon(dTO.getIcon())
                .build();
    }
    public IncomeDTO toDTO(IncomeEntity entity){
        return IncomeDTO.builder()
                .name(entity.getName())
                .amount(entity.getAmount())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId(): null)
                .categoryName(entity.getCategory() != null? entity.getCategory().getName(): "N/A")
                .build();
    }
}
