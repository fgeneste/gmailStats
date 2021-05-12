import { Component, Vue, Inject } from 'vue-property-decorator';

import { IConf, Conf } from '@/shared/model/conf.model';
import ConfService from './conf.service';

const validations: any = {
  conf: {
    key: {},
    value: {},
  },
};

@Component({
  validations,
})
export default class ConfUpdate extends Vue {
  @Inject('confService') private confService: () => ConfService;
  public conf: IConf = new Conf();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.confId) {
        vm.retrieveConf(to.params.confId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.conf.id) {
      this.confService()
        .update(this.conf)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.conf.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.confService()
        .create(this.conf)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.conf.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveConf(confId): void {
    this.confService()
      .find(confId)
      .then(res => {
        this.conf = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
