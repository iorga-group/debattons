package com.iorga.debattons.domain.validation;

import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.enumeration.ReactionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckRootTypeReactionWithoutParentValidator implements ConstraintValidator<CheckRootTypeReactionWithoutParent, Reaction> {
   public void initialize(CheckRootTypeReactionWithoutParent constraint) {
   }

   public boolean isValid(Reaction reaction, ConstraintValidatorContext context) {
       boolean valid = true;
       if (reaction.getParentReaction() != null && ReactionType.ROOT.equals(reaction.getType())) {
           context.disableDefaultConstraintViolation();
           context.buildConstraintViolationWithTemplate("Cannot create a root reaction with parent")
               .addPropertyNode("type")
               .addConstraintViolation();
           valid = false;
       }
      return valid;
   }
}
