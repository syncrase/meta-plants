import { element, by, ElementFinder } from 'protractor';

export class GerminationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-germination div table .btn-danger'));
  title = element.all(by.css('gp-germination div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class GerminationUpdatePage {
  pageTitle = element(by.id('gp-germination-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  tempsDeGerminationInput = element(by.id('field_tempsDeGermination'));
  conditionDeGerminationInput = element(by.id('field_conditionDeGermination'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTempsDeGerminationInput(tempsDeGermination: string): Promise<void> {
    await this.tempsDeGerminationInput.sendKeys(tempsDeGermination);
  }

  async getTempsDeGerminationInput(): Promise<string> {
    return await this.tempsDeGerminationInput.getAttribute('value');
  }

  async setConditionDeGerminationInput(conditionDeGermination: string): Promise<void> {
    await this.conditionDeGerminationInput.sendKeys(conditionDeGermination);
  }

  async getConditionDeGerminationInput(): Promise<string> {
    return await this.conditionDeGerminationInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class GerminationDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-germination-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-germination'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
