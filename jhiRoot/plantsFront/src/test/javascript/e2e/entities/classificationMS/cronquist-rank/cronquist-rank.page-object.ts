import { element, by, ElementFinder } from 'protractor';

export class CronquistRankComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-cronquist-rank div table .btn-danger'));
  title = element.all(by.css('perma-cronquist-rank div h2#page-heading span')).first();
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

export class CronquistRankUpdatePage {
  pageTitle = element(by.id('perma-cronquist-rank-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  rankSelect = element(by.id('field_rank'));

  parentSelect = element(by.id('field_parent'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRankSelect(rank: string): Promise<void> {
    await this.rankSelect.sendKeys(rank);
  }

  async getRankSelect(): Promise<string> {
    return await this.rankSelect.element(by.css('option:checked')).getText();
  }

  async rankSelectLastOption(): Promise<void> {
    await this.rankSelect.all(by.tagName('option')).last().click();
  }

  async parentSelectLastOption(): Promise<void> {
    await this.parentSelect.all(by.tagName('option')).last().click();
  }

  async parentSelectOption(option: string): Promise<void> {
    await this.parentSelect.sendKeys(option);
  }

  getParentSelect(): ElementFinder {
    return this.parentSelect;
  }

  async getParentSelectedOption(): Promise<string> {
    return await this.parentSelect.element(by.css('option:checked')).getText();
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

export class CronquistRankDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-cronquistRank-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-cronquistRank'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
