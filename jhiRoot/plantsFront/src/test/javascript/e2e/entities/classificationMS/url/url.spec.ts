import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { UrlComponentsPage, UrlDeleteDialog, UrlUpdatePage } from './url.page-object';

const expect = chai.expect;

describe('Url e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let urlComponentsPage: UrlComponentsPage;
  let urlUpdatePage: UrlUpdatePage;
  let urlDeleteDialog: UrlDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Urls', async () => {
    await navBarPage.goToEntity('url');
    urlComponentsPage = new UrlComponentsPage();
    await browser.wait(ec.visibilityOf(urlComponentsPage.title), 5000);
    expect(await urlComponentsPage.getTitle()).to.eq('Urls');
    await browser.wait(ec.or(ec.visibilityOf(urlComponentsPage.entities), ec.visibilityOf(urlComponentsPage.noResult)), 1000);
  });

  it('should load create Url page', async () => {
    await urlComponentsPage.clickOnCreateButton();
    urlUpdatePage = new UrlUpdatePage();
    expect(await urlUpdatePage.getPageTitle()).to.eq('Create or edit a Url');
    await urlUpdatePage.cancel();
  });

  it('should create and save Urls', async () => {
    const nbButtonsBeforeCreate = await urlComponentsPage.countDeleteButtons();

    await urlComponentsPage.clickOnCreateButton();

    await promise.all([urlUpdatePage.setUrlInput('url'), urlUpdatePage.cronquistRankSelectLastOption()]);

    await urlUpdatePage.save();
    expect(await urlUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await urlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Url', async () => {
    const nbButtonsBeforeDelete = await urlComponentsPage.countDeleteButtons();
    await urlComponentsPage.clickOnLastDeleteButton();

    urlDeleteDialog = new UrlDeleteDialog();
    expect(await urlDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Url?');
    await urlDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(urlComponentsPage.title), 5000);

    expect(await urlComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
