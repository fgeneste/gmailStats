import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IStat } from '@/shared/model/stat.model';

import StatService from './stat.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Stat extends Vue {
  @Inject('statService') private statService: () => StatService;
  private removeId: number = null;

  public stats: IStat[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllStats();
  }

  public clear(): void {
    this.retrieveAllStats();
  }

  public retrieveAllStats(): void {
    this.isFetching = true;

    this.statService()
      .retrieve()
      .then(
        res => {
          this.stats = res.data;
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

  public prepareRemove(instance: IStat): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeStat(): void {
    this.statService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gmailStatsApp.stat.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllStats();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
