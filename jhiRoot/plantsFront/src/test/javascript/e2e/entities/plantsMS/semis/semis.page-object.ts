import { element, by, ElementFinder } from 'protractor';

export class SemisComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-semis div table .btn-danger'));
  title = element.all(by.css('perma-semis div h2#page-heading span')).first();
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
    return this.title.getText();
  }
}

export class SemisUpdatePage {
  pageTitle = element(by.id('perma-semis-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));

  semisPleineTerreSelect = element(by.id('field_semisPleineTerre'));
  semisSousAbrisSelect = element(by.id('field_semisSousAbris'));
  typeSemisSelect = element(by.id('field_typeSemis'));
  germinationSelect = element(by.id('field_germination'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async semisPleineTerreSelectLastOption(): Promise<void> {
    await this.semisPleineTerreSelect.all(by.tagName('option')).last().click();
  }

  async semisPleineTerreSelectOption(option: string): Promise<void> {
    await this.semisPleineTerreSelect.sendKeys(option);
  }

  getSemisPleineTerreSelect(): ElementFinder {
    return this.semisPleineTerreSelect;
  }

  async getSemisPleineTerreSelectedOption(): Promise<string> {
    return await this.semisPleineTerreSelect.element(by.css('option:checked')).getText();
  }

  async semisSousAbrisSelectLastOption(): Promise<void> {
    await this.semisSousAbrisSelect.all(by.tagName('option')).last().click();
  }

  async semisSousAbrisSelectOption(option: string): Promise<void> {
    await this.semisSousAbrisSelect.sendKeys(option);
  }

  getSemisSousAbrisSelect(): ElementFinder {
    return this.semisSousAbrisSelect;
  }

  async getSemisSousAbrisSelectedOption(): Promise<string> {
    return await this.semisSousAbrisSelect.element(by.css('option:checked')).getText();
  }

  async typeSemisSelectLastOption(): Promise<void> {
    await this.typeSemisSelect.all(by.tagName('option')).last().click();
  }

  async typeSemisSelectOption(option: string): Promise<void> {
    await this.typeSemisSelect.sendKeys(option);
  }

  getTypeSemisSelect(): ElementFinder {
    return this.typeSemisSelect;
  }

  async getTypeSemisSelectedOption(): Promise<string> {
    return await this.typeSemisSelect.element(by.css('option:checked')).getText();
  }

  async germinationSelectLastOption(): Promise<void> {
    await this.germinationSelect.all(by.tagName('option')).last().click();
  }

  async germinationSelectOption(option: string): Promise<void> {
    await this.germinationSelect.sendKeys(option);
  }

  getGerminationSelect(): ElementFinder {
    return this.germinationSelect;
  }

  async getGerminationSelectedOption(): Promise<string> {
    return await this.germinationSelect.element(by.css('option:checked')).getText();
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

export class SemisDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-semis-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-semis'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
