import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class ReactionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('dbt-reaction div table .btn-danger'));
  title = element.all(by.css('dbt-reaction div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ReactionUpdatePage {
  pageTitle = element(by.id('dbt-reaction-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  titleInput = element(by.id('field_title'));
  contentInput = element(by.id('field_content'));
  typeSelect = element(by.id('field_type'));
  typeLevelInput = element(by.id('field_typeLevel'));
  supportScoreInput = element(by.id('field_supportScore'));
  creatorSelect = element(by.id('field_creator'));
  parentReactionSelect = element(by.id('field_parentReaction'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleInput(title) {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput() {
    return await this.titleInput.getAttribute('value');
  }

  async setContentInput(content) {
    await this.contentInput.sendKeys(content);
  }

  async getContentInput() {
    return await this.contentInput.getAttribute('value');
  }

  async setTypeSelect(type) {
    await this.typeSelect.sendKeys(type);
  }

  async getTypeSelect() {
    return await this.typeSelect.element(by.css('option:checked')).getText();
  }

  async typeSelectLastOption(timeout?: number) {
    await this.typeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setTypeLevelInput(typeLevel) {
    await this.typeLevelInput.sendKeys(typeLevel);
  }

  async getTypeLevelInput() {
    return await this.typeLevelInput.getAttribute('value');
  }

  async setSupportScoreInput(supportScore) {
    await this.supportScoreInput.sendKeys(supportScore);
  }

  async getSupportScoreInput() {
    return await this.supportScoreInput.getAttribute('value');
  }

  async creatorSelectLastOption(timeout?: number) {
    await this.creatorSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async creatorSelectOption(option) {
    await this.creatorSelect.sendKeys(option);
  }

  getCreatorSelect(): ElementFinder {
    return this.creatorSelect;
  }

  async getCreatorSelectedOption() {
    return await this.creatorSelect.element(by.css('option:checked')).getText();
  }

  async parentReactionSelectLastOption(timeout?: number) {
    await this.parentReactionSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async parentReactionSelectOption(option) {
    await this.parentReactionSelect.sendKeys(option);
  }

  getParentReactionSelect(): ElementFinder {
    return this.parentReactionSelect;
  }

  async getParentReactionSelectedOption() {
    return await this.parentReactionSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ReactionDeleteDialog {
  private dialogTitle = element(by.id('dbt-delete-reaction-heading'));
  private confirmButton = element(by.id('dbt-confirm-delete-reaction'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
