package com.notspend.cotroller;

import com.notspend.entity.Category;
import com.notspend.entity.User;
import com.notspend.service.persistance.CategoryService;
import com.notspend.service.persistance.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("allexpense")
    public String listExpenseCategories (Model model){
        List<Category> categories = categoryService.getAllExpenseCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryType", "expense");
        return "category/all";
    }

    @GetMapping("allincome")
    public String listIncomeCategories (Model model){
        List<Category> categories = categoryService.getAllIncomeCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryType", "income");
        return "category/all";
    }

    @GetMapping("add")
    public String addCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "category/add";
    }

    @PostMapping("addProcess")
    public String processAddCategoryForm(@Valid @ModelAttribute("category") Category category,
                                         BindingResult bindingResult, User user,
                                         Model model){
        if (bindingResult.hasErrors()){
            return "category/add";
        }
        if (categoryService.isCategoryNameExist(category)){
            model.addAttribute("category", category);
            return "category/nameexist";
        }
        category.setUser(user);
        categoryService.addCategory(category);
        return "success";
    }

    @GetMapping("delete")
    public String deleteCategory(@ModelAttribute("categoryId") int categoryId, Model model){
        //Category for deleting
        Category category = categoryService.getCategory(categoryId);
        boolean isIncomeCategory = category.isIncome();

        //check if this category is not last expense or income category
        if (isIncomeCategory && categoryService.getAllIncomeCategories().size() <= 1){
            return "category/cantdeletelastcategory";
        } else if (categoryService.getAllExpenseCategories().size() <= 1){
            return "category/cantdeletelastcategory";
        }

        //check if there is relationship on category
        if (categoryService.isCategoryHaveRelations(categoryId)){
            //inform User that category can't be deleted
            //and propose to create new category
            //or transfer expenses to exist category
            Category categoryToDelete = categoryService.getCategory(categoryId);
            List<Category> categories = categoryService.getAllExpenseCategories();
            categories.remove(categoryToDelete);

            Category categoryToReplace = new Category();
            model.addAttribute("categoryToDelete", categoryToDelete);
            model.addAttribute("categories", categories);
            model.addAttribute("category", categoryToReplace);
            return "category/cantdelete";
        } else {
            categoryService.deleteCategoryById(categoryId);
            return "success";
        }
    }

    @GetMapping("update")
    public String updateCategory(@ModelAttribute("categoryId") int categoryId, Model model){
        Category category = categoryService.getCategory(categoryId);
        model.addAttribute("category", category);
        return "category/update";
    }

    @PostMapping("updateprocess")
    public String updateProcess(@ModelAttribute("category") Category category, User user) {
        //when update category User for some reason null
        //todo: rework this
        category.setUser(user);
        categoryService.updateCategory(category);
        if (category.isIncome()){
            return "redirect:allincome";
        } else {
            return "redirect:allexpense";
        }
    }

    @PostMapping("transferToExistCategory")
    public String transferToOtherCategoryAndDelete(@ModelAttribute("categoryId") int toCategoryId,
                                                   @ModelAttribute("categoryToDelete") int fromCategoryId,
                                                   Model model){
        categoryService.replaceCategoryInAllExpenses(fromCategoryId, toCategoryId);
        return "success";
    }

    @PostMapping("transferToNewCategory")
    public String transferToNewCategoryAndDelete(@ModelAttribute("newCategory") Category category,
                                                 @ModelAttribute("categoryToDelete") int fromCategoryId,
                                                 User user,
                                                 Model model){
        category.setUser(user);
        categoryService.addCategory(category);
        categoryService.replaceCategoryInAllExpenses(fromCategoryId, category.getId());
        return "success";
    }
}
