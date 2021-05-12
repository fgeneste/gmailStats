import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IConf } from '@/shared/model/conf.model';

import ConfService from './conf.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Conf extends Vue {
  @Inject('confService') private confService: () => ConfService;
  private removeId: number = null;

  public confs: IConf[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllConfs();
  }

  public clear(): void {
    this.retrieveAllConfs();
  }

  public retrieveAllConfs(): void {
    this.isFetching = true;

    this.confService()
      .retrieve()
      .then(
        res => {
          this.confs = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IConf): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeConf(): void {
    this.confService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gmailStatsApp.conf.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllConfs();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
