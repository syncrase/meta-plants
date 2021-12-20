import { element, by, ElementFinder } from 'protractor';

export class UrlComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-url div table .btn-danger'));
  title = element.all(by.css('perma-url div h2#page-heading span')).first();
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

export class UrlUpdatePage {
  pageTitle = element(by.id('perma-url-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  urlInput = element(by.id('field_url'));

  cronquistRankSelect = element(by.id('field_cronquistRank'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setUrlInput(url: string): Promise<void> {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput(): Promise<string> {
    return await this.urlInput.getAttribute('value');
  }

  async cronquistRankSelectLastOption(): Promise<void> {
    await this.cronquistRankSelect.all(by.tagName('option')).last().click();
  }

  async cronquistRankSelectOption(option: string): Promise<void> {
    await this.cronquistRankSelect.sendKeys(option);
  }

  getCronquistRankSelect(): ElementFinder {
    return this.cronquistRankSelect;
  }

  async getCronquistRankSelectedOption(): Promise<string> {
    return await this.cronquistRankSelect.element(by.css('option:checked')).getText();
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

export class UrlDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-url-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-url'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
