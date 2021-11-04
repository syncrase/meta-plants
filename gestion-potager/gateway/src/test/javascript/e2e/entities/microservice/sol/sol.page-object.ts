import { element, by, ElementFinder } from 'protractor';

export class SolComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-sol div table .btn-danger'));
  title = element.all(by.css('gp-sol div h2#page-heading span')).first();
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

export class SolUpdatePage {
  pageTitle = element(by.id('gp-sol-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  phMinInput = element(by.id('field_phMin'));
  phMaxInput = element(by.id('field_phMax'));
  typeInput = element(by.id('field_type'));
  richesseInput = element(by.id('field_richesse'));

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

  async setPhMinInput(phMin: string): Promise<void> {
    await this.phMinInput.sendKeys(phMin);
  }

  async getPhMinInput(): Promise<string> {
    return await this.phMinInput.getAttribute('value');
  }

  async setPhMaxInput(phMax: string): Promise<void> {
    await this.phMaxInput.sendKeys(phMax);
  }

  async getPhMaxInput(): Promise<string> {
    return await this.phMaxInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async setRichesseInput(richesse: string): Promise<void> {
    await this.richesseInput.sendKeys(richesse);
  }

  async getRichesseInput(): Promise<string> {
    return await this.richesseInput.getAttribute('value');
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

export class SolDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-sol-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-sol'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('gpTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
