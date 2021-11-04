import { element, by, ElementFinder } from 'protractor';

export class ExpositionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-exposition div table .btn-danger'));
  title = element.all(by.css('gp-exposition div h2#page-heading span')).first();
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
    return this.title.getAttribute('gpTranslate');
  }
}

export class ExpositionUpdatePage {
  pageTitle = element(by.id('gp-exposition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  valeurInput = element(by.id('field_valeur'));
  ensoleilementInput = element(by.id('field_ensoleilement'));

  planteSelect = element(by.id('field_plante'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('gpTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setValeurInput(valeur: string): Promise<void> {
    await this.valeurInput.sendKeys(valeur);
  }

  async getValeurInput(): Promise<string> {
    return await this.valeurInput.getAttribute('value');
  }

  async setEnsoleilementInput(ensoleilement: string): Promise<void> {
    await this.ensoleilementInput.sendKeys(ensoleilement);
  }

  async getEnsoleilementInput(): Promise<string> {
    return await this.ensoleilementInput.getAttribute('value');
  }

  async planteSelectLastOption(): Promise<void> {
    await this.planteSelect.all(by.tagName('option')).last().click();
  }

  async planteSelectOption(option: string): Promise<void> {
    await this.planteSelect.sendKeys(option);
  }

  getPlanteSelect(): ElementFinder {
    return this.planteSelect;
  }

  async getPlanteSelectedOption(): Promise<string> {
    return await this.planteSelect.element(by.css('option:checked')).getText();
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

export class ExpositionDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-exposition-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-exposition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('gpTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
