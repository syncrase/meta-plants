import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SousGenreComponentsPage, SousGenreDeleteDialog, SousGenreUpdatePage } from './sous-genre.page-object';

const expect = chai.expect;

describe('SousGenre e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sousGenreComponentsPage: SousGenreComponentsPage;
  let sousGenreUpdatePage: SousGenreUpdatePage;
  let sousGenreDeleteDialog: SousGenreDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SousGenres', async () => {
    await navBarPage.goToEntity('sous-genre');
    sousGenreComponentsPage = new SousGenreComponentsPage();
    await browser.wait(ec.visibilityOf(sousGenreComponentsPage.title), 5000);
    expect(await sousGenreComponentsPage.getTitle()).to.eq('Sous Genres');
    await browser.wait(ec.or(ec.visibilityOf(sousGenreComponentsPage.entities), ec.visibilityOf(sousGenreComponentsPage.noResult)), 1000);
  });

  it('should load create SousGenre page', async () => {
    await sousGenreComponentsPage.clickOnCreateButton();
    sousGenreUpdatePage = new SousGenreUpdatePage();
    expect(await sousGenreUpdatePage.getPageTitle()).to.eq('Create or edit a Sous Genre');
    await sousGenreUpdatePage.cancel();
  });

  it('should create and save SousGenres', async () => {
    const nbButtonsBeforeCreate = await sousGenreComponentsPage.countDeleteButtons();

    await sousGenreComponentsPage.clickOnCreateButton();

    await promise.all([
      sousGenreUpdatePage.setNomFrInput('nomFr'),
      sousGenreUpdatePage.setNomLatinInput('nomLatin'),
      sousGenreUpdatePage.genreSelectLastOption(),
      sousGenreUpdatePage.sousGenreSelectLastOption(),
    ]);

    await sousGenreUpdatePage.save();
    expect(await sousGenreUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sousGenreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SousGenre', async () => {
    const nbButtonsBeforeDelete = await sousGenreComponentsPage.countDeleteButtons();
    await sousGenreComponentsPage.clickOnLastDeleteButton();

    sousGenreDeleteDialog = new SousGenreDeleteDialog();
    expect(await sousGenreDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sous Genre?');
    await sousGenreDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sousGenreComponentsPage.title), 5000);

    expect(await sousGenreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
