import { element, by, ElementFinder } from 'protractor';

export class AllelopathieComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-allelopathie div table .btn-danger'));
  title = element.all(by.css('perma-allelopathie div h2#page-heading span')).first();
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

export class AllelopathieUpdatePage {
  pageTitle = element(by.id('perma-allelopathie-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  typeInput = element(by.id('field_type'));
  descriptionInput = element(by.id('field_description'));
  impactInput = element(by.id('field_impact'));

  cibleSelect = element(by.id('field_cible'));
  origineSelect = element(by.id('field_origine'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setImpactInput(impact: string): Promise<void> {
    await this.impactInput.sendKeys(impact);
  }

  async getImpactInput(): Promise<string> {
    return await this.impactInput.getAttribute('value');
  }

  async cibleSelectLastOption(): Promise<void> {
    await this.cibleSelect.all(by.tagName('option')).last().click();
  }

  async cibleSelectOption(option: string): Promise<void> {
    await this.cibleSelect.sendKeys(option);
  }

  getCibleSelect(): ElementFinder {
    return this.cibleSelect;
  }

  async getCibleSelectedOption(): Promise<string> {
    return await this.cibleSelect.element(by.css('option:checked')).getText();
  }

  async origineSelectLastOption(): Promise<void> {
    await this.origineSelect.all(by.tagName('option')).last().click();
  }

  async origineSelectOption(option: string): Promise<void> {
    await this.origineSelect.sendKeys(option);
  }

  getOrigineSelect(): ElementFinder {
    return this.origineSelect;
  }

  async getOrigineSelectedOption(): Promise<string> {
    return await this.origineSelect.element(by.css('option:checked')).getText();
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

export class AllelopathieDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-allelopathie-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-allelopathie'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
