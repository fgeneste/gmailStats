import FgenesteService from './fgeneste.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Fgeneste extends Vue {
  @Inject('fgenesteService') private fgenesteService: () => FgenesteService;
  @Inject('accountService') private accountService: () => AccountService;

  private hasAnyAuthorityValue = false;

  public mounted(): void {
  }

  public viderTable(e) {
    this.fgenesteService().resetMails()
      .then(value => {});
  }

  public recupererMessages(e) {
    this.fgenesteService().getMails()
      .then(value => {});
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValue = value;
      });
    return this.hasAnyAuthorityValue;
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

}
