import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CronquistRankComponentsPage, CronquistRankDeleteDialog, CronquistRankUpdatePage } from './cronquist-rank.page-object';

const expect = chai.expect;

describe('CronquistRank e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cronquistRankComponentsPage: CronquistRankComponentsPage;
  let cronquistRankUpdatePage: CronquistRankUpdatePage;
  let cronquistRankDeleteDialog: CronquistRankDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CronquistRanks', async () => {
    await navBarPage.goToEntity('cronquist-rank');
    cronquistRankComponentsPage = new CronquistRankComponentsPage();
    await browser.wait(ec.visibilityOf(cronquistRankComponentsPage.title), 5000);
    expect(await cronquistRankComponentsPage.getTitle()).to.eq('Cronquist Ranks');
    await browser.wait(
      ec.or(ec.visibilityOf(cronquistRankComponentsPage.entities), ec.visibilityOf(cronquistRankComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CronquistRank page', async () => {
    await cronquistRankComponentsPage.clickOnCreateButton();
    cronquistRankUpdatePage = new CronquistRankUpdatePage();
    expect(await cronquistRankUpdatePage.getPageTitle()).to.eq('Create or edit a Cronquist Rank');
    await cronquistRankUpdatePage.cancel();
  });

  it('should create and save CronquistRanks', async () => {
    const nbButtonsBeforeCreate = await cronquistRankComponentsPage.countDeleteButtons();

    await cronquistRankComponentsPage.clickOnCreateButton();

    await promise.all([cronquistRankUpdatePage.rankSelectLastOption(), cronquistRankUpdatePage.parentSelectLastOption()]);

    await cronquistRankUpdatePage.save();
    expect(await cronquistRankUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cronquistRankComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CronquistRank', async () => {
    const nbButtonsBeforeDelete = await cronquistRankComponentsPage.countDeleteButtons();
    await cronquistRankComponentsPage.clickOnLastDeleteButton();

    cronquistRankDeleteDialog = new CronquistRankDeleteDialog();
    expect(await cronquistRankDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Cronquist Rank?');
    await cronquistRankDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cronquistRankComponentsPage.title), 5000);

    expect(await cronquistRankComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
